package lesionprobe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import jviewmda.ExpandingCanvas;
import jviewmda.JNifti;
import jviewmda.Mda;
import jviewmda.ViewmdaWidget;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author magland
 */
public class LPViewWidget extends GridPane {

	List<ViewmdaWidget> m_views = new ArrayList<>();
	boolean m_show_arrows = true;
	String m_data_path;
	String m_id;
	int[][] m_arrow_coordinates;

	LPViewWidget() {

		for (int i = 0; i < 4; i++) {
			m_views.add(new ViewmdaWidget());
		}

		add(m_views.get(0), 1, 1);
		add(m_views.get(1), 2, 1);
		add(m_views.get(2), 1, 2);
		add(m_views.get(3), 2, 2);

		//sync the slices
		for (ViewmdaWidget view : m_views) {
			ViewmdaWidget view0 = view;
			view0.setTopControlsVisible(false);
			view0.setBottomControlsVisible(false);
			view0.setCursorVisible(false);
			view0.onCurrentSliceChanged(() -> {
				int[] ind0 = view0.currentIndex();
				for (ViewmdaWidget view2 : m_views) {
					view2.setCurrentIndex(ind0);
				}
			});
			view0.customCanvas("test");
			view0.onImageRefreshed(() -> draw_overlay(view0));
		}

	}

	public void setDataPath(String path) {
		m_data_path = path;
	}

	public void setDataId(String id) {
		m_id = id;
	}
	
	public void setArrowCoordinates(int[][] coords) {
		m_arrow_coordinates=coords.clone();
	}

	public void refreshViews() {
		refresh_views();
	}

	public void setShowArrows(boolean val) {
		if (m_show_arrows == val) {
			return;
		}
		m_show_arrows = val;
		for (ViewmdaWidget view : m_views) {
			draw_overlay(view);
		}
	}

	private void refresh_views() {
		List<String> image_paths = new ArrayList<>();
		image_paths.add(m_data_path + "/Images/" + m_id + "_FLAIR.nii");
		image_paths.add(m_data_path + "/Images/" + m_id + "_PD.nii");
		image_paths.add(m_data_path + "/Images/" + m_id + "_T1.nii");
		image_paths.add(m_data_path + "/Images/" + m_id + "_T2.nii");

		for (int i = 0; i < image_paths.size(); i++) {
			if (i < m_views.size()) {
				Mda X = load_array(image_paths.get(i));
				m_views.get(i).setArray(X);
			}
		}
	}

	private void draw_overlay(ViewmdaWidget view) {
		ExpandingCanvas CC = view.customCanvas("test");
		GraphicsContext gc = CC.getGraphicsContext2D();
		gc.clearRect(0, 0, CC.getWidth(), CC.getHeight());
		if (m_show_arrows) {
			List<Color> colors=new ArrayList<>();
			colors.add(Color.YELLOW); colors.add(Color.LIGHTBLUE);
			double[] mean0 = new double[2];
			mean0[0] = 0;
			mean0[1] = 0;
			for (int i = 0; i < m_arrow_coordinates.length; i++) {
				mean0[0] += m_arrow_coordinates[i][0];
				mean0[1] += m_arrow_coordinates[i][1];
			}
			if (m_arrow_coordinates.length > 0) {
				mean0[0] /= m_arrow_coordinates.length;
				mean0[1] /= m_arrow_coordinates.length;
			}
			for (int i = 0; i < m_arrow_coordinates.length; i++) {
				String dir = "left";
				if (m_arrow_coordinates[i][0] > mean0[0]) {
					dir = "right";
				}
				draw_arrow(view, m_arrow_coordinates[i][0], m_arrow_coordinates[i][1], dir,colors.get(i % colors.size()));
			}
		}
	}

	private void draw_arrow(ViewmdaWidget view, int x, int y, String direction,Color color) {
		ExpandingCanvas CC = view.customCanvas("test");
		GraphicsContext gc = CC.getGraphicsContext2D();
		gc.setStroke(color);
		gc.setLineWidth(3);
		int[] pix1 = view.indexToPixel(x, y);
		int[] pix2 = new int[2];
		int[] pix3 = new int[2];
		int[] pix4 = new int[2];
		int sgn = 1;
		if (direction == "right") {
			sgn = -1;
		}

		pix2[0] = pix1[0] - 20 * sgn;
		pix2[1] = pix1[1];
		pix3[0] = pix1[0] - 5 * sgn;
		pix3[1] = pix1[1] + 6;
		pix4[0] = pix1[0] - 5 * sgn;
		pix4[1] = pix1[1] - 6;

		gc.strokeLine(pix1[0], pix1[1], pix2[0], pix2[1]);
		gc.strokeLine(pix1[0], pix1[1], pix3[0], pix3[1]);
		gc.strokeLine(pix1[0], pix1[1], pix4[0], pix4[1]);
	}

	private Mda load_array(String path0) {
		Mda X = new Mda();
		String suf = FilenameUtils.getExtension(path0);
		if (suf.equals("mda")) {
			if (!X.read(path0)) {
				System.err.println("Problem reading mda file.");
			}
		} else if ((suf.equals("nii")) || (suf.equals("gz"))) {
			JNifti Y = new JNifti();
			try {
				Y.read(path0);
				X = Y.array();
			} catch (IOException ee) {
				ee.printStackTrace();
				System.err.println("Unable to read nifti file.");
			}
		} else {
			System.err.println("Unrecognized file type: " + suf);
		}
		return X;
	}
}
