package blackjack;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Card {
	
		private String name;
		private int pts;
		private ImageIcon icon;
		
		public Card(String name) {
			this.name = name;
			createValue();
			spawnImage();
			
		}
		
		private void createValue() {
			String[] nameParts = this.name.split("_");
			this.pts = Integer.parseInt(nameParts[0]);		
		}
		private void spawnImage() {
			try {
			this.icon = new ImageIcon(Card.class.getResource("/imagesCard/" + this.name));
			// "/imagesCard/2_clubs.png"
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null, "Images not found");
				System.exit(0);
			}
		}
		public String getName() {
			return this.name;
		}
		
		public int getPts() {
			return this.pts;
		}
		public ImageIcon getIcon() {
			return this.icon;
		}
		@Override
		public String toString() {
		    return this.name + " = " + this.pts;
		}

	}
