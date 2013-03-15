package tw.kewang.ui.captcha;

import java.security.SecureRandom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CaptchaView extends TextView {
	private static final Typeface[] TYPEFACES = { Typeface.DEFAULT,
			Typeface.DEFAULT_BOLD, Typeface.MONOSPACE, Typeface.SANS_SERIF,
			Typeface.SERIF };

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

		buildPaint();
		buildCaptcha();
	}

	public String getCaptcha() {
		return captcha;
	}

	public void refresh() {
		buildPaint();
		buildCaptcha();

		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();

		canvas.drawColor(Color.rgb(random.nextInt(256), random.nextInt(256),
				random.nextInt(256)));

		// text
		for (int i = 0; i < 4; i++) {
			String s = String.valueOf(captcha.charAt(i));
			int x = width / 4 * i;
			int y = 100;

			paint.setTypeface(TYPEFACES[random.nextInt(5)]);

			canvas.save();

			canvas.rotate(random.nextInt(180) - 90, x, y);
			canvas.drawText(s, x, y, paint);

			canvas.restore();
		}

		// line
		Paint linePaint = new Paint();
		for (int i = 0; i < 10; i++) {
			linePaint.setColor(Color.rgb(random.nextInt(256),
					random.nextInt(256), random.nextInt(256)));
			linePaint.setAntiAlias(true);
			linePaint.setStrokeWidth(random.nextInt(5));
			linePaint.setStyle(Paint.Style.STROKE);

			canvas.drawLine(random.nextInt(width), random.nextInt(height),
					random.nextInt(width), random.nextInt(height), linePaint);
		}

		// dot
		Paint dotPaint = new Paint();
		for (int i = 0; i < width * height * 0.005; i++) {
			dotPaint.setColor(Color.rgb(random.nextInt(256),
					random.nextInt(256), random.nextInt(256)));
			dotPaint.setStrokeWidth(3);
			dotPaint.setAntiAlias(true);
			dotPaint.setStyle(Paint.Style.STROKE);

			canvas.drawPoint(random.nextInt(width), random.nextInt(height),
					dotPaint);
		}
	}

	private void buildPaint() {
		int color = Color.rgb(random.nextInt(256), random.nextInt(256),
				random.nextInt(256));

		paint.setColor(color);
		paint.setAntiAlias(true);
		paint.setTextSize(100);
		paint.setStyle(Paint.Style.STROKE);
	}

	private void buildCaptcha() {
		captcha = String.format("%1$04d", random.nextInt(10000));
	}
}