package lesionprobe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import jviewmda.ExpandingCanvas;
import jviewmda.JNifti;
import jviewmda.Mda;
import jviewmda.ViewmdaWidget;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author magland
 */
public class LPMainWidget extends HBox {

	
	String m_data_path;
	LPViewWidget m_view_widget;
	LPControlPanel m_control_panel;

	LPMainWidget() {
		m_control_panel=new LPControlPanel();
		m_control_panel.setMinWidth(300);
		m_control_panel.setMaxWidth(300);
		
		m_view_widget=new LPViewWidget();
		
		getChildren().addAll(m_control_panel,m_view_widget);
		
	}

	public void setDataPath(String path) {
		m_data_path = path;
		m_view_widget.setDataPath(path);
	}
	public void do_test() {
		int[][] coords=new int[2][3];
		coords[0][0]=50; coords[0][1]=50; coords[0][2]=30;
		coords[1][0]=90; coords[1][1]=80; coords[1][2]=30;
		m_view_widget.setArrowCoordinates(coords);
		m_view_widget.setDataId("ID001");
		m_view_widget.refreshViews();
		
		m_control_panel.setNumArrows(coords.length);
		m_control_panel.refresh();
	}

	///////////////////// PRIVATE /////////////////////////////
	
	
	
	

	
}
