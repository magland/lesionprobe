
package lesionprobe;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author magland
 */
public class LPControlPanel extends VBox {
	int m_num_arrows=1;
	List<String> m_arrow_color_names=new ArrayList<>();

	LPControlPanel() {
		m_arrow_color_names.add("Yellow");
		m_arrow_color_names.add("Blue");
		
		this.setPadding(new Insets(20,20,20,20));
	}
	public void setNumArrows(int num) {
		m_num_arrows=num;
	}
	public void refresh() {
		this.getChildren().removeAll(getChildren());
		
		for (int i=0; ((i<m_num_arrows)&&(i<m_arrow_color_names.size())); i++) {
			String col=m_arrow_color_names.get(i);
			Text L=new Text("Does the "+col+" arrow point to a lesion?");
			L.setWrappingWidth(this.minWidth(100));
			this.getChildren().add(L);
			
			{
				HBox spacer=new HBox(); spacer.setMinHeight(8);
				this.getChildren().add(spacer);
			}
			
			HBox hbox=new HBox();
			ToggleGroup TG = new ToggleGroup();
			RadioButton RBY=new RadioButton();
			RBY.setText("Yes");
			RBY.setToggleGroup(TG);
			hbox.getChildren().add(RBY);
			RadioButton RBN=new RadioButton();
			{
				HBox spacer=new HBox(); spacer.setMinWidth(12);
				hbox.getChildren().add(spacer);
			}
			RBN.setText("No");
			RBN.setToggleGroup(TG);
			hbox.getChildren().add(RBN);
			this.getChildren().add(hbox);
			
			{
				HBox spacer=new HBox(); spacer.setMinHeight(40);
				this.getChildren().add(spacer);
			}
		}
		
		Button submit_button=new Button("SUBMIT");
		this.getChildren().add(submit_button);
	}
}
