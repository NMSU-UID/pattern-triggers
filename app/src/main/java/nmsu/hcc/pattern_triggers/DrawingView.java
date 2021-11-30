package nmsu.hcc.pattern_triggers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.vision.digitalink.DigitalInkRecognition;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions;
import com.google.mlkit.vision.digitalink.Ink;

import nmsu.hcc.pattern_triggers.listeners.LatestBitmapImageListener;
import nmsu.hcc.pattern_triggers.listeners.ParsedTextListener;

public class DrawingView extends View {

    public int width;
    public  int height;
    Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;

    LatestBitmapImageListener latestBitmapImageListener;

    Ink.Builder inkBuilder = Ink.builder();
    Ink.Stroke.Builder strokeBuilder;
    Ink ink;

    DigitalInkRecognitionModelIdentifier modelIdentifier = null;
    DigitalInkRecognitionModel model;
    DigitalInkRecognizer recognizer;

    ParsedTextListener parsedTextListener;

    RemoteModelManager remoteModelManager = RemoteModelManager.getInstance();

    public DrawingView(Context c) {
        super(c, null);
    }

    public DrawingView(Context c, AttributeSet attrs) {
        super(c, attrs);
        initiate();
    }

    public DrawingView(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        initiate();
    }

    public void initiate(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);

        setDrawingCacheEnabled(true);

        // Specify the recognition model for a language
        try {
            modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US");
        } catch (MlKitException e) {
            // language tag failed to parse, handle error.
        }

        model = DigitalInkRecognitionModel.builder(modelIdentifier).build();

        // Get a recognizer for the language
        recognizer =
                DigitalInkRecognition.getClient(
                        DigitalInkRecognizerOptions.builder(model).build());

        remoteModelManager
                .download(model, new DownloadConditions.Builder().build())
                .addOnSuccessListener(aVoid -> Log.e("DrawingView", "Model downloaded"))
                .addOnFailureListener(
                        e -> Log.e("DrawingView", "Error while downloading a model: " + e));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath,  mPaint);
        canvas.drawPath(circlePath,  circlePaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        circlePath.reset();
        // commit the path to our offscreen
        mCanvas.drawPath(mPath,  mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    public void getLatestBitmapImage(LatestBitmapImageListener latestBitmapImageListener){
        this.latestBitmapImageListener = latestBitmapImageListener;
        //this.latestBitmapImageListener.latestBitmapImage(getDrawingCache());
        //clearDrawing();
    }

    public void getParsedTextListener(ParsedTextListener parsedTextListener){
        this.parsedTextListener = parsedTextListener;
    }

    public void clearDrawing() {
        setDrawingCacheEnabled(false);
        onSizeChanged(width, height, width, height);
        invalidate();

        setDrawingCacheEnabled(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("onTouchEvent", "Action Down");
                touch_start(x, y);
                invalidate();
                strokeBuilder = Ink.Stroke.builder();
                strokeBuilder.addPoint(Ink.Point.create(x, y));
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("onTouchEvent", "Action Move");
                touch_move(x, y);
                invalidate();
                strokeBuilder.addPoint(Ink.Point.create(x, y));
                break;
            case MotionEvent.ACTION_UP:
                Log.e("onTouchEvent", "Action Up");
                touch_up();
                //Bitmap bitmap = getDrawingCache();
                //this.latestBitmapImageListener.latestBitmapImage(bitmap);
                invalidate();
                //clearDrawing();

                strokeBuilder.addPoint(Ink.Point.create(x, y));
                inkBuilder.addStroke(strokeBuilder.build());
                strokeBuilder = null;
                break;
        }

        ink = inkBuilder.build();
        recognizer.recognize(ink)
                .addOnSuccessListener(result -> {
                            this.parsedTextListener.parsedText(result.getCandidates().get(0).getText());
                            ink = null;
                        })
                .addOnFailureListener(
                        e -> Log.e("DrawingView", "Error during recognition: " + e));
        clearDrawing();
        return true;
    }
}