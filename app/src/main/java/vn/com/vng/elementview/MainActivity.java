package vn.com.vng.elementview;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.com.vng.elementview.element.*;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        setupRecycler();

    }

    private void setupRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(createAdapter());
    }


    private RecyclerView.Adapter createAdapter() {
        return new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                switch (viewType){
                    case 0:
                        return new ElementDemoHolder(buildElementViewDemo2());
                    default:
                        return new NativeDemoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_demo, parent, false));
                }
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 40;
            }

            @Override
            public int getItemViewType(int position) {
                return position % 2;
            }
        };
    }



    private class ElementDemoHolder extends RecyclerView.ViewHolder {
        public ElementDemoHolder(View itemView) {
            super(itemView);
        }

    }

    private class NativeDemoHolder extends RecyclerView.ViewHolder {
        public NativeDemoHolder(View itemView) {
            super(itemView);
        }
    }



    private View buildElementViewDemo() {
        ElementView view = new ElementView(this);
        view.setRootElement(buildRootElement());
        return view;
    }

    private View buildElementViewDemo2(){
        ElementView view = new ElementView(this);
        view.setRootElement(buildDemoElement2());
        return view;
    }

    private Element buildDemoElement2() {
        LinearElementGroup root = new LinearElementGroup(this, Element.MATCH_PARENT, Element.WRAP_CONTENT);
        root.setOrientation(LinearElementGroup.ORIENTATION_HORIZONTAL);
        root.setPadding(dpToPx(8));

        ImageElement imageElement = new ImageElement(this, dpToPx(60), dpToPx(60));
        imageElement.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.demo_img));
        imageElement.setScaleType(ImageElement.ScaleType.CENTER_CROP);

        TextElement textElement = new TextElement(this);
        textElement.setMargin(dpToPx(4));
        textElement.setForegroundGravity(Gravity.VERTICAL_CENTER);
        textElement.setText("Text text text text 123");
        textElement.setMaxLines(1);
        textElement.setEllipsize(TextUtils.TruncateAt.END);

        root.addElement(imageElement);
        root.addElement(textElement);
        return root;
    }


    private Element buildRootElement() {
        //create root\
        LinearElementGroup root0 = new LinearElementGroup(this);
        root0.setGravity(Gravity.CENTER);

        LinearElementGroup root =new LinearElementGroup(this);
        root.setOrientation(LinearElementGroup.ORIENTATION_HORIZONTAL);
        root.setPadding((int) (4*getResources().getDisplayMetrics().density));


        //build child1
        LinearElementGroup group1 = new LinearElementGroup(this);
        group1.setOrientation(LinearElementGroup.ORIENTATION_VERTICAL);
        group1.setGravity(Gravity.CENTER);

        TextElement textElement1 = new TextElement(this);
        textElement1.setText("This");
        textElement1.setPadding((int) (12*getResources().getDisplayMetrics().density));
        textElement1.setTextSize((int) (16*getResources().getDisplayMetrics().scaledDensity));
        textElement1.setMaxLines(2);
        textElement1.setEllipsize(TextUtils.TruncateAt.END);
        textElement1.setBackgroundColor(0xffAAAAAA);

//        TextElement textElement2 = new TextElement(this);
//        textElement2.setText("Motor element2");

        group1.addElement(textElement1);
//        group1.addElement(textElement2);

        //build child2
        TextElement textElement3 = new TextElement(this);
        textElement3.setText("TextElement3");
        textElement3.setAlignment(Layout.Alignment.ALIGN_CENTER);
        textElement3.setMargin((int) (8*getResources().getDisplayMetrics().density));
        textElement3.setBackgroundColor(0xffAAAAAA);

//        textElement3.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.corner_shape));
//        textElement3.setMargin((int) (8*getResources().getDisplayMetrics().density));



        //build child3
        TextElement textElement4 = new TextElement(this);
        textElement4.setText("Hello ajinomotor");
        textElement4.setPadding((int) (16 * getResources().getDisplayMetrics().density));
        textElement4.setTextColor(0xff880011);



        //add to root
        root.addElement(group1, 1);
        root.addElement(textElement3);
        root.addElement(textElement4);


        ImageElement imageElement = new ImageElement(this, 480, 480);
//        imageElement.setPadding(12);
        imageElement.setScaleType(ImageElement.ScaleType.CENTER_CROP);
        imageElement.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.demo_img));
        imageElement.setRoundCorner(ImageElement.ROUND_CIRCLE);


        root0.addElement(root);
        root0.addElement(imageElement);

        return root0;
    }


    private int dpToPx(int dp){
        return (int) (getResources().getDisplayMetrics().density * dp);
    }

    private int spToPx(int sp){
        return (int) (getResources().getDisplayMetrics().scaledDensity * sp);
    }


}
