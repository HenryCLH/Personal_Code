package com.henry.account;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final File type_file = new File(Environment.getExternalStorageDirectory(), "typedata.txt");
                if (type_file.exists()) {
                    final View input_view = getLayoutInflater().inflate(R.layout.input, null);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    String[] type = getFile(type_file);
                    dialog.setTitle("新增账目");
                    final Spinner spinner = (Spinner) input_view.findViewById(R.id.spinner);
                    ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, type);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    dialog.setView(input_view);
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String type_selected = spinner.getSelectedItem().toString();
                            EditText value = (EditText) input_view.findViewById(R.id.value_input);
                            String val = value.getText().toString();
                            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");
                            Date date = new Date(System.currentTimeMillis());
                            String date_str = formatter.format(date);
                            if (val.length() != 0) {
                                float tmp = (float) (Math.round(Float.valueOf(val)*100))/100;
                                val = Float.toString(tmp);
                                String data = type_selected + ";" + date_str + ";" + val + ";";
                                writeFile(data, "listdata.txt", false);
                                Toast.makeText(MainActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                            } else {
                                writeFile("", "listdata.txt", false);
                                Toast.makeText(MainActivity.this, "金额不能为空！", Toast.LENGTH_SHORT).show();
                            }
                            refresh();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(MainActivity.this, "请先设置账目分类！", Toast.LENGTH_SHORT).show();
                    refresh();
                }
            }
        });
        final File data_file = new File(Environment.getExternalStorageDirectory(), "listdata.txt");
        if(data_file != null){
            refresh();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            final View view = getLayoutInflater().inflate(R.layout.setting,null);
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("设置账目分类");
            dialog.setView(view);
            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText edit_text = (EditText) view.findViewById(R.id.setting_input);
                    String input = edit_text.getText().toString();
                    if (input.isEmpty()) {
                        Toast.makeText(MainActivity.this, "输入不能为空!", Toast.LENGTH_SHORT).show();
                    } else {
                        writeFile(input, "typedata.txt", true);
                        final File list_file = new File(Environment.getExternalStorageDirectory(), "listdata.txt");
                        if (list_file.exists()) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("！")
                                    .setMessage("是否删除原有记录？")
                                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            list_file.delete();
                                            refresh();
                                        }
                                    })
                                    .setNegativeButton("否", null)
                                    .show();
                        }
                        Toast.makeText(MainActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
                        refresh();
                    }
                    try {
                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialog, false);
                    } catch (Exception e) {
                        System.out.println("Exception in setting's Button!");
                    }
                }
            });
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh(){
        try {
            final File type_file = new File(Environment.getExternalStorageDirectory(),"typedata.txt");
            final File list_file = new File(Environment.getExternalStorageDirectory(),"listdata.txt");
            final String []type = getFile(type_file);
            final String []list = getFile(list_file);
            if((list!=null)&&(list.length!=0)) {
                ExpandableListAdapter adapter = new BaseExpandableListAdapter() {
                    int num = list.length / 3;
                    String[][] data = trans(list);
                    @Override
                    public int getGroupCount() { return type.length;}
                    @Override
                    public int getChildrenCount(int groupPosition) {
                        int count = 0;
                        for (int i = 0; i < num; i++) {
                            if (data[i][0].equals(type[groupPosition])) {
                                count++;
                            }
                        }
                        return count;
                    }
                    @Override
                    public String[] getGroup(int groupPosition) {
                        int child_num = getChildrenCount(groupPosition);
                        if (child_num != 0) {
                            String[] out = new String[child_num];
                            int i = 0;
                            int j = 0;
                            while (j < child_num) {
                                if (type[groupPosition].equals(data[i][0])) {
                                    out[j++] = data[i][1] + ";" + data[i][2];
                                }
                                i++;
                            }
                            return out;
                        } else {
                            String[] out = null;
                            return out;
                        }
                    }
                    @Override
                    public Object getChild(int groupPosition, int childPosition) {
                        String out = new String();
                        int i = 0;
                        while ((childPosition >= 0) && (getChildrenCount(groupPosition) != 0)) {
                            if (type[groupPosition].equals(data[i][0])) {
                                childPosition--;
                            }
                            i++;
                        }
                        out = data[i - 1][1] + "       ￥" + data[i - 1][2];
                        return out;
                    }
                    @Override
                    public long getGroupId(int groupPosition) { return groupPosition;}
                    @Override
                    public long getChildId(int groupPosition, int childPosition) { return childPosition;}
                    @Override
                    public boolean hasStableIds() { return false;}
                    @Override
                    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                        LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        TextView textView = getGroupTextView();
                        textView.setText("  " + type[groupPosition] + "       Total: ￥" + Float.toString(getSum(groupPosition)));
                        linearLayout.addView(textView);
                        return linearLayout;
                    }
                    @Override
                    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                        TextView textView = getChildTextView(groupPosition, childPosition);
                        if (getChildrenCount(groupPosition) != 0) {
                            textView.setText(getChild(groupPosition, childPosition).toString());
                        }
                        return textView;
                    }
                    @Override
                    public boolean isChildSelectable(int groupPosition, int childPosition) { return false;}

                    public String[][] trans(String[] in) {
                        if (num == 0) {
                            String[][] out = null;
                            return out;
                        } else {
                            String[][] out = new String[num][3];
                            for (int i = 0; i < num; i++) {
                                out[i][0] = in[i * 3];
                                out[i][1] = in[i * 3 + 1];
                                out[i][2] = in[i * 3 + 2];
                            }
                            return out;
                        }
                    }

                    public TextView getChildTextView(final int groupPosition, final int childPosition) {
                        TextView textView = new TextView(MainActivity.this);
                        textView.setGravity(Gravity.LEFT);
                        textView.setPadding(36, 0, 0, 0);
                        textView.setTextSize(20);
                        textView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("!")
                                        .setMessage("修改或删除该条记录")
                                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                change(groupPosition, childPosition, "");
                                            }
                                        })
                                        .setNegativeButton("修改", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                final EditText editText = new EditText(MainActivity.this);
                                                new AlertDialog.Builder(MainActivity.this)
                                                        .setTitle("!")
                                                        .setMessage("请输入修改金额")
                                                        .setView(editText)
                                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                String val = editText.getText().toString();
                                                                if(val.isEmpty()){
                                                                    Toast.makeText(MainActivity.this, "金额不能为空！修改失败！", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else{
                                                                    change(groupPosition, childPosition, val);
                                                                }
                                                            }
                                                        })
                                                        .show();
                                            }
                                        })
                                        .show();
                                return false;
                            }
                        });
                        return textView;
                    }

                    public TextView getGroupTextView() {
                        TextView textView = new TextView(MainActivity.this);
                        textView.setGravity(Gravity.LEFT);
                        textView.setPadding(36, 0, 0, 0);
                        textView.setTextSize(30);
                        return textView;
                    }

                    public float getSum(int groupPosition) {
                        String[] tmp = getGroup(groupPosition);
                        if (tmp != null) {
                            String[][] ttmp = new String[tmp.length][2];
                            for (int i = 0; i < tmp.length; i++) {
                                ttmp[i] = tmp[i].split(";");
                            }
                            float out = 0;
                            for (int i = 0; i < ttmp.length; i++) {
                                out += Float.valueOf(ttmp[i][1]);
                            }
                            out = (float) (Math.round(out*100))/100;
                            return out;
                        } else {
                            return 0;
                        }
                    }

                    public void change(int groupPosition, int childPosition, String value){
                        String back_data = "";
                        for(int i = 0; i < num; i++){
                            if(data[i][0].equals(type[groupPosition])){
                                if(childPosition != 0){
                                    back_data += data[i][0];
                                    back_data += ";";
                                    back_data += data[i][1];
                                    back_data += ";";
                                    back_data += data[i][2];
                                    back_data += ";";
                                }
                                else{
                                    if(value.length() != 0){
                                        back_data += data[i][0];
                                        back_data += ";";
                                        back_data += data[i][1];
                                        back_data += ";";
                                        back_data += value;
                                        back_data += ";";
                                    }
                                }
                                childPosition--;
                            }
                            else{
                                back_data += data[i][0];
                                back_data += ";";
                                back_data += data[i][1];
                                back_data += ";";
                                back_data += data[i][2];
                                back_data += ";";
                            }
                        }
                        writeFile(back_data, "listdata.txt", true);
                        if(value.length() == 0){
                            Toast.makeText(MainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        }
                        refresh();
                    }
                };
                ExpandableListView expandListView = (ExpandableListView) findViewById(R.id.listView);
                expandListView.setAdapter(adapter);
            }
            else{
                ExpandableListAdapter adapter = new BaseExpandableListAdapter(){
                    @Override
                    public int getGroupCount() { return 0;}
                    @Override
                    public int getChildrenCount(int groupPosition) { return 0;}
                    @Override
                    public Object getGroup(int groupPosition) { return null;}
                    @Override
                    public Object getChild(int groupPosition, int childPosition) { return null;}
                    @Override
                    public long getGroupId(int groupPosition) { return groupPosition;}
                    @Override
                    public long getChildId(int groupPosition, int childPosition) { return 0;}
                    @Override
                    public boolean hasStableIds() { return false;}
                    @Override
                    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                        LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        TextView textView = getGroupTextView();
                        textView.setText("  " + type[groupPosition] + "       Total: 0.0");
                        linearLayout.addView(textView);
                        return linearLayout;
                    }
                    @Override
                    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) { return null;}
                    @Override
                    public boolean isChildSelectable(int groupPosition, int childPosition) { return false;}
                    public TextView getGroupTextView() {
                        TextView textView = new TextView(MainActivity.this);
                        textView.setGravity(Gravity.LEFT);
                        textView.setPadding(36, 0, 0, 0);
                        textView.setTextSize(30);
                        return textView;
                    }
                };
                ExpandableListView expandListView = (ExpandableListView) findViewById(R.id.listView);
                expandListView.setAdapter(adapter);
            }
        } catch (Exception e) {
            System.out.println("Exception in refresh()!");
        }
    }

    public void writeFile(String write_data, String file_name, boolean whether_delete) {
        File file = new File(Environment.getExternalStorageDirectory(),file_name);
        try{
            if(file.exists()) {
                if (whether_delete) {
                    file.delete();
                    file.createNewFile();
                }
            }
            else{
                file.createNewFile();
            }
            FileOutputStream file_out = new FileOutputStream(file, true);
            file_out.write(write_data.getBytes());
            file_out.close();
        }catch(Exception e){
            System.out.println("Exception in writeFile()!");
        }
    }

    public String[] getFile(File file) {
        try {
            FileInputStream file_in = new FileInputStream(file);
            int length = file_in.available();
            byte []buffer = new byte[length];
            file_in.read(buffer);
            String tmp = new String(buffer);
            String []data = tmp.split(";");
            file_in.close();
            return data;
        } catch (Exception e) {
            System.out.println("Exception in getFile()!");
        }
        return null;
    }

}
