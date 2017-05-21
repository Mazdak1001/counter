package com.sepehr.sa_sh.abfacounter01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rey.material.widget.Spinner;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.CounterReportService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ICounterReportService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IOnOffloadService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.IStatisticsRepo;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.OnOffloadService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.StatisticsRepo;
import com.sepehr.sa_sh.abfacounter01.Logic.IOffloadLogic;
import com.sepehr.sa_sh.abfacounter01.Logic.OffloadLogic;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by _1101 on 10/20/2016.
 */
public class UrgentOffloadFragment extends Fragment {

    Context mContext;
    Button mStartButton;
    ProgressBar mProgressBar;
    TextView mStateTextView;
    Spinner mSpinner;

    int userCode;
    String token;
    String deviceId;

    IReadingConfigService readingConfigService;
    IStatisticsRepo statisticsRepo;
    IOnOffloadService onOffloadService;
    ICounterReportService counterReportService;
    IToastAndAlertBuilder toastAndAlertBuilder;

    IOffloadLogic offloadLogic;

    public UrgentOffloadFragment() {
    }
    //Button urgentOffLoadBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_urgent_offload, container, false);
        initialize(rootView);
        //region bluetooth-old
       /* List<CounterReadingModel01> counterReadingModel01List=
                CounterReadingModel01.listAll(CounterReadingModel01.class);
        String fileString="";
        for (CounterReadingModel01 readingModel:counterReadingModel01List) {
            fileString=generateOffloadRow(readingModel)+"\n"+fileString;
        }
        final File txtFile=getOutputMediaFile();
        String filePath= writeToFile(fileString, txtFile);
        urgentOffLoadBtn=(Button)rootView.findViewById(R.id.urgent_off_load_btn);
        urgentOffLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(txtFile));
                    startActivity(intent);
                }
                catch (Exception e){
                    Log.e("send error :",e.getMessage());
                }
            }
        });*/
        //endregion
        return rootView;
    }

    private void initialize(View rootView){
        mContext=getContext();
        mProgressBar=(ProgressBar) rootView.findViewById(R.id.progressBar);
        mSpinner=(Spinner)rootView.findViewById(R.id.listNumberSpinner);
        mStartButton=(Button)rootView.findViewById(R.id.offLoadButton);
        mStateTextView=(TextView)rootView.findViewById(R.id.offLoadStateTextView);
        userCode=((UrgentActivity)getContext()).getUserCode();
        token=((UrgentActivity)getContext()).getToken();
        deviceId= Build.SERIAL;
        toastAndAlertBuilder=new ToastAndAlertBuilder(mContext);
        readingConfigService=new ReadingConfigService();
        statisticsRepo=new StatisticsRepo(mContext);
        onOffloadService=new OnOffloadService(mContext);
        counterReportService=new CounterReportService();
        offloadLogic=new OffloadLogic(mContext,mStartButton,mProgressBar,mStateTextView,mSpinner,userCode,token,deviceId,
                readingConfigService,toastAndAlertBuilder,statisticsRepo,onOffloadService,counterReportService);
        startOffload();
    }

    private void startOffload(){
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offloadLogic.start(true);
            }
        });
    }

    //region old-bluetooth
   /* private String generateOffloadRow(CounterReadingModel01 readingModel){
        String radif =readingModel.Radif==null?"0":readingModel.Radif.longValue()+"";
        String paddedRadif =  String.format("%9s", radif).replace(' ', '0');

        String counterNumber =readingModel.CounterNumber==null?"0":
                readingModel.CounterNumber.toString();
        String paddedCounterNumber = String.format("%7s",counterNumber ).replace(' ', '0');

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        String paddedUserCode="0"+prefs.getInt("userCode", 0);

        String counterState=readingModel.counterRealState==null?"0":
                readingModel.counterRealState.toString();
        String paddedCounterState=String.format("%2s",counterState).replace(' ', '0');

        String date=readingModel.RegistrationDateJalali==null?"0":
                readingModel.RegistrationDateJalali;
        String paddedDate= String.format("%6s",date).replace(' ', '0');

        String reportCode="00";
        return paddedRadif+paddedCounterNumber+paddedUserCode
                +paddedCounterState+paddedDate+reportCode;
    }
    //
    private void writeToFile(String data,Context context,File textFile) {
        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput(textFile.getAbsolutePath(), Context.MODE_PRIVATE));
                    //new OutputStreamWriter(context.openFileOutput("xxxx.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.e("write file", " succeeded");
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    //
   private String writeToFile(String data,File file) {
       try {
           FileOutputStream f = new FileOutputStream(file);
           PrintWriter pw = new PrintWriter(f);
           pw.write(data);
           pw.flush();
           pw.close();
           f.close();
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
       finally {
           return file.getAbsolutePath();
       }
   }
    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "output");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("out", "Oops! Failed create "
                        + "out" + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File textFile;

        textFile = new File(mediaStorageDir.getPath() + File.separator
                + "OUT_" + timeStamp + ".txt");

        return textFile;
    }*/
    //endregion
}
