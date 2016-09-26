package qianfeng.filemangerapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ListView lv;

    private List<File> list;  // list作为全局变量，如果在不需要数据叠加的时候，记得要清空噢！

    private File rootFile;

    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = ((ListView) findViewById(R.id.lv));
        list = new ArrayList<>();


        rootFile  = Environment.getExternalStorageDirectory();
        Log.d("qianfeng:", "onCreate:--------->  " + rootFile.toString());

        File[] files = rootFile.listFiles();

        arrayList(rootFile,list); // 获取下一级目录的所有文件及文件夹/**/
        lv.setAdapter(new MyAdapter(this,list));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                rootFile = list.get(position);
                if(rootFile.isDirectory()) {

//                    rootFile = list.get(position);
                    // 如果点击了的话，那么这个item就是作为根目录
                    arrayList(rootFile, list);
                    myAdapter = new MyAdapter(MainActivity.this,list);
                    lv.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();

                }else
                {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile(rootFile),"audio/mp3");
//                    startActivity(intent);
                    if(rootFile.getName().toLowerCase().endsWith(".mp3"))
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(rootFile),"audio/*"); //  audio/mp3
                        startActivity(intent);

                    }
                }
                // lv.setSelection(0);  设置ListView自动滑动到第0个item，默认是滑倒最底部
                lv.setSelection(0);
            }
        });

    }

    private void arrayList(File rootFile,List<File> list)
    {
        list.clear();
        File[] files = rootFile.listFiles();
        for(File f : files)
        {
            list.add(f);
        }

    }

    @Override
    public void onBackPressed()
    {
        // 这是一行在退出时，非常重要的代码.
        rootFile = rootFile.isFile()?rootFile.getParentFile():rootFile; // 三目表达式还有一个返回值，需要接收

        // 如果当前已点击的目录，是根目录，就退出程序
        if(rootFile.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath()))
        {
            super.onBackPressed();
        }else
        {
            // 加载上一级的所有内容
            rootFile = rootFile.getParentFile();
            arrayList(rootFile,list);
            myAdapter = new MyAdapter(this,list);
            lv.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
        }
    }





}

