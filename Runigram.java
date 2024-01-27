
// This class uses the Color class, which is part of a package called awt,
// which is part of Java's standard class library.
import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
		// ...
	}

	// This method reads data from a PPM file and stores it in a 2D array of color objects.
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				int red = in.readInt();
				int green = in.readInt();
				int blue = in.readInt();
				image[i][j] = new Color(red, green, blue);
			}
		}
		return image;
	}

	// Prints the RGB values of a given color.
	public static void print(Color c) {
		System.out.print("(");
		System.out.printf("%3s,", c.getRed()); // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
		System.out.printf("%3s", c.getBlue()); // Prints the blue component
		System.out.print(")  ");
	}

	// This method prints the RGB values of a given color object.
	public static void print(Color[][] image) {
		int numRows = image.length;
		int numCols = image[0].length;
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				print(image[i][j]);
			}
			System.out.println();
		}
	}

	//  Produces a horizontally flipped version of the given image.
	public static Color[][] flippedHorizontally(Color[][] image) {
		Color[][] fhImage = new Color[image.length][image[0].length];
		for (int i = 0; i < image.length; i++) {
			for (int j = image[0].length - 1; j >= 0; j--) {
				fhImage[i][image[0].length - 1 - j] = image[i][j];
			}
		}
		return fhImage;
	}

	// Produces a vertically flipped version of the given image.
	public static Color[][] flippedVertically(Color[][] image) {
		Color[][] fvImage = new Color[image.length][image[0].length];
		for (int i = image.length - 1; i >= 0; i--) {
			for (int j = 0; j < image[0].length; j++) {
				fvImage[image.length - 1 - i][j] = image[i][j];
			}
		}
		return fvImage;
	}

	// Computes the luminance of a given color.
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object
	// consisting the three values r = lum, g = lum, b = lum.
	public static Color luminance(Color pixel) {
		int red = pixel.getRed();
		int green = pixel.getGreen();
		int blue = pixel.getBlue();
		int lum = (int) ((0.299 * red) + (0.587 * green) + (0.114 * blue));
		return new Color(lum, lum, lum);
	}

	// Produces a grayscale version of the given image.
	public static Color[][] grayScaled(Color[][] image) {
		Color[][] gsImage = new Color[image.length][image[0].length];
		for (int i = 0; i < gsImage.length; i++) {
			for (int j = 0; j < gsImage[0].length; j++) {
				gsImage[i][j] = luminance(image[i][j]);
			}
		}
		return gsImage;
	}

	//  Scales (resizes) the given image to the specified width and height.
	public static Color[][] scaled(Color[][] image, int width, int height) {
		Color[][] scaled = new Color[height][width];
		double x = (double) image[0].length / width;
		double y = (double) image.length / height;
		for (int i = 0; i < scaled.length; i++) {
			for (int j = 0; j < scaled[0].length; j++) {
				scaled[i][j] = image[(int) (i * y)][(int) (j * x)];
			}
		}
		return scaled;
	}

	// Blends two colors using a linear combination.
	public static Color blend(Color c1, Color c2, double alpha) {
		int red = (int) (alpha * c1.getRed() + (1 - alpha) * c2.getRed());
		int green = (int) (alpha * c1.getGreen() + (1 - alpha) * c2.getGreen());
		int blue = (int) (alpha * c1.getBlue() + (1 - alpha) * c2.getBlue());
		return new Color(red, green, blue);
	}

	// Blends two images using a linear combination of pixel colors.
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		Color[][] blended = new Color[image1.length][image1[0].length];
		for (int i = 0; i < blended.length; i++) {
			for (int j = 0; j < blended[0].length; j++) {
				blended[i][j] = blend(image1[i][j], image2[i][j], alpha);
			}
		}
		return blended;
	}

	// Morphs a source image into a target image gradually over a specified number of steps, displaying each intermediate step.
	public static void morph(Color[][] source, Color[][] target, int n) {
		Color[][] morphed = new Color[source.length][source[0].length];
		for (int i = 0; i < n; i++) {
			morphed = blend(source, target, (double) i / (n - 1));
			display(morphed);
			StdDraw.pause(500);
		}
	}

	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(height, width);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
		// Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor(image[i][j].getRed(),
						image[i][j].getGreen(),
						image[i][j].getBlue());
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}
