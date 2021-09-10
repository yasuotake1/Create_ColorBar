import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.NewImage;
import ij.plugin.PlugIn;

public class Create_RGBTriangle implements PlugIn {
	private static final String[] choiceBg = { "White", "Black" };
	private static final double sin120 = 0.866;
	private static final double cos120 = -0.5;
	public static int size = 64;
	private static double height;
	private static double yGrav;

	public void run(String arg) {
		GenericDialog gd = new GenericDialog("Create RGB triangle");
		gd.addNumericField("Size: ", size, 0);
		gd.addRadioButtonGroup("Background: ", choiceBg, 1, 2, choiceBg[0]);
		gd.showDialog();
		if (gd.wasCanceled())
			return;

		size = (int) gd.getNextNumber();
		height = sin120 * size;
		yGrav = height * 0.666;
		int intHeight = (int) height;
		int fill = NewImage.FILL_WHITE;
		if (gd.getNextRadioButton() == choiceBg[1])
			fill = NewImage.FILL_BLACK;
		ImagePlus imp = NewImage.createRGBImage("RGBtriangle", size, intHeight, 1, fill);
		int[] pixels = (int[]) imp.getProcessor().getPixels();
		int r, g, b;
		for (int i = 0; i < intHeight; i++) {
			for (int j = 0; j < size; j++) {
				r = calcRed(j, i);
				g = calcGreen(j, i);
				b = calcBlue(j, i);
				if (r >= 0 && g >= 0 && b >= 0)
				pixels[i * size + j] = (r << 16) + (g << 8) + b;
			}
		}
		imp.show();
	}

	private int calcRed(int x, int y) {
		return (int) ((height - 1 - y) / (height - 1) * 255);
	}

	private int calcGreen(int x, int y) {
		double yRot = sin120 * ((double) x - size / 2) + cos120 * ((double) y - yGrav) + yGrav;
		return (int) ((height - 1 - yRot) / (height - 1) * 255);
	}

	private int calcBlue(int x, int y) {
		double yRot = cos120 * ((double) y - yGrav) - sin120 * ((double) x - size / 2) + yGrav;
		return (int) ((height - 1 - yRot) / (height - 1) * 255);
	}

}