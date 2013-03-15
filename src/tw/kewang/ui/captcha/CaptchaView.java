package tw.kewang.ui.captcha;

import java.security.SecureRandom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author kewang
 */
public class CaptchaView extends TextView {
	private static final Typeface[] TYPEFACES = { Typeface.DEFAULT,
			Typeface.DEFAULT_BOLD, Typeface.MONOSPACE, Typeface.SANS_SERIF,
			Typeface.SERIF };
	private static final Style[] STYLES = { Style.FILL, Style.FILL_AND_STROKE,
			Style.STROKE };

	private String captcha;
	private Paint paint;
	private SecureRandom random;

	public CaptchaView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	public CaptchaView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public CaptchaView(Context context) {
		super(context);

		init();
	}

	private void init() {
		paint = new Paint();
		random = new SecureRandom();

		buildCaptcha();
	}

	/**
	 * get captcha string
	 * 
	 * @return
	 */
	public String getCaptcha() {
		return captcha;
	}

	/**
	 * refresh captcha string
	 */
	public void refresh() {
		buildCaptcha();

		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();

		canvas.drawColor(getColor());

		// text
		for (int i = 0; i < 4; i++) {
			String s = String.valueOf(captcha.charAt(i));
			int x = width / 4 * i;
			int y = 100;

			canvas.save();

			canvas.rotate(random.nextInt(180) - 90, x, y);

			paint.setColor(getColor());
			paint.setAntiAlias(true);
			paint.setTextSize(100);
			paint.setStyle(STYLES[random.nextInt(3)]);
			paint.setTypeface(TYPEFACES[random.nextInt(5)]);

			canvas.drawText(s, x, y, paint);

			canvas.restore();
		}

		// line
		for (int i = 0; i < width * height * 0.00005; i++) {
			paint.setColor(getColor());
			paint.setAntiAlias(true);
			paint.setStrokeWidth(random.nextInt(5));
			paint.setStyle(STYLES[random.nextInt(3)]);

			canvas.drawLine(random.nextInt(width), random.nextInt(height),
					random.nextInt(width), random.nextInt(height), paint);
		}

		// dot
		for (int i = 0; i < width * height * 0.005; i++) {
			paint.setColor(getColor());
			paint.setStrokeWidth(3);
			paint.setAntiAlias(true);
			paint.setStyle(STYLES[random.nextInt(3)]);

			canvas.drawPoint(random.nextInt(width), random.nextInt(height),
					paint);
		}
	}

	private int getColor() {
		return Color.rgb(random.nextInt(256), random.nextInt(256),
				random.nextInt(256));
	}

	private void buildCaptcha() {
		captcha = String.format("%1$04d", random.nextInt(10000));
	}
}