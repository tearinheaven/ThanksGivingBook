package com.zte.thanksbook.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

public class AudioRecorder {
	
	/**
	 * 最大录音时长
	 */
	private static final long MAX_DURATION = 299900;
	
	/**
	 * 当前用户名
	 */
	private String userName;
	
	/**
	 * 开始时间 
	 */
	private long startTime;
	
	/**
	 * 当前录音开始时间
	 */
	private long currentStartTime;
	
	/**
	 * 当前录音文件名
	 */
	private String fileName;
	
	/**
	 * 录音时长 ms
	 */
	private long duration;
	
	/**
	 * 上一个录音文件名称
	 */
	private String lastFileName;
	
	/**
	 * 上一个录音的时长 ms
	 */
	private long lastDuration;
	
	/**
	 * 录音实例
	 */
	private MediaRecorder recorder = null;
	
	/**
	 * 监听实例
	 */
	private AudioRecorderListener listener;
	
	//private RecordTask recordTask = null;
	
	private boolean isRecording;
	
	/**
	 * 录音监听器
	 */
	public interface AudioRecorderListener{
		//结束录音
		public void onRecorderStop();
	}
	
	public AudioRecorder(String userName, AudioRecorderListener audioRecorderListener) {
		this.userName = userName;
		this.startTime = Long.valueOf(new Date().getTime());;
		this.fileName = null;
		this.duration = 0;
		this.lastFileName = null;
		this.recorder = null;
		this.listener = audioRecorderListener;
	}
	
	public void release()
	{
		this.lastFileName = null;
		this.recorder = null;

		this.deleteCurrentFile();
	}
	
	/**
	 * 开始录音
	 */
	public void startRecording()
	{		
		//如果当前文件名不为空，则为断点续录
		if (!TGUtil.isEmpty(this.fileName))
		{
			this.lastFileName = this.fileName;
			this.lastDuration = this.duration;
		}

		this.currentStartTime = Long.valueOf(new Date().getTime());
		this.fileName = getFilePath();
		if (!TGUtil.isEmpty(this.fileName))
		{
			//开始录音
	        this.recorder = new MediaRecorder();
	        this.recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        this.recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);	        
	        this.recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	        Log.v("ThanksBook", "录音文件路径：" + this.fileName);
	        this.recorder.setOutputFile(this.fileName);

	        try {
	        	this.recorder.prepare();
	        } catch (IOException e) {
	            Log.e("ThanksBook", "录音实例 prepare() 失败");
	            e.printStackTrace();
	        }

        	/*this.recordTask = null;
        	this.recordTask = new RecordTask();
	        this.recordTask.execute(new Void[]{});*/
	        
	        new RecordTask().execute(new Void[]{});
	        
	        //this.recorder.start();
	        
	        //启动后台控制录音长度的线程
	        //new RecordTimeControlTask().execute(new Void[]{});
		}
		else
		{
			Log.e("ThanksBook", "录音文件路径获取失败！");
		}
	}
	
	/**
	 * 结束录音
	 */
	public synchronized void stopRecording()
	{
		if (this.isRecording)
		{
			Log.v("ThanksBook", "结束录音");
			this.isRecording = false;	
		}
	}
	
	/**
	 * 结束录音
	 */
	public synchronized void doStopRecording()
	{
		if (this.recorder!=null)
		{
			this.recorder.stop();
			long endTime = Long.valueOf(new Date().getTime());
			this.duration = endTime - this.currentStartTime;
			Log.v("ThanksBoos", "录音时长：" + this.duration);
			this.recorder.release();
			this.recorder = null;
			
			//如果是断点续录，则组合文件
			if (!TGUtil.isEmpty(this.lastFileName))
			{
				try {
					this.currentStartTime = Long.valueOf(new Date().getTime());
					String newFile = this.getFilePath();
		            FileOutputStream fos = new FileOutputStream(new File(newFile));  
		            RandomAccessFile ra = null;  
		            ra = new RandomAccessFile(this.lastFileName, "r");
	                byte[] buffer = new byte[1024 * 8];  
	                int len = 0;  
	                while ((len = ra.read(buffer)) != -1) {  
	                    fos.write(buffer, 0, len);  
	                }
	                ra.close();

	                RandomAccessFile ra1 = new RandomAccessFile(this.fileName, "r");  
	                ra1.seek(6);  
	                buffer = new byte[1024 * 8];  
	                len = 0;  
	                while ((len = ra1.read(buffer)) != -1) {  
	                    fos.write(buffer, 0, len);  
	                }  	                
	                ra1.close();
	                
		            fos.close();
		            
		            //删除旧的两个文件
		            new File(this.lastFileName).delete();
		            new File(this.fileName).delete();
		            
		            this.fileName = newFile;
		        } catch (Exception e) {  
		        	e.printStackTrace();
		        } 
				
				this.duration = this.lastDuration + this.duration;
			}
		}
	}
	
	private class RecordTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			isRecording = true;
			
			Log.v("ThanksBook", "录音进程开始");
			recorder.start();

			long thisDuration = 0;				
			long thisTime = Long.valueOf(new Date().getTime());
			thisDuration = lastDuration + (thisTime - currentStartTime);
			while (isRecording && thisDuration < MAX_DURATION) 
			{
				try {
					Thread.sleep(200);
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
				thisTime = Long.valueOf(new Date().getTime());
				thisDuration = lastDuration + (thisTime - currentStartTime);
			};

			Log.v("ThanksBook", "录音进程检测到录音截止");
			doStopRecording();
			return null;
		}
		
		@Override  
        protected void onPostExecute(Void result) { 		
			if (null != listener)
			{
				Log.v("ThanksBook", "录音进程结束，更新UI");
				listener.onRecorderStop();
			}
        }
	};
	
	/*private class RecordTimeControlTask extends AsyncTask<Void, Integer, Void> {
		//控制时长为60秒
		@Override
		protected Void doInBackground(Void... params) {
			Log.v("ThanksBook", "录音控制进程开始");
			
			try {
				long thisDuration = 0;				
				long thisTime = Long.valueOf(new Date().getTime());
				thisDuration = lastDuration + (thisTime - currentStartTime);
				while (thisDuration < MAX_DURATION) 
				{
					if (isRecording)
					{
						Thread.sleep(1000);
						thisTime = Long.valueOf(new Date().getTime());
						thisDuration = lastDuration + (thisTime - currentStartTime);
						//Log.v("ThanksBook", "录音时长：" + thisDuration);
					}
				};
			} catch (Exception ex)
			{
				Log.e("ThanksBook", "录音控制进程失败");
				ex.printStackTrace();
			}
			return null;
		}
		
		@Override  
        protected void onPostExecute(Void result) { 
			if (isRecording)
			{
				stopRecording();
			}
        }  
	};*/
	
	/**
	 * 获取录音文件名
	 */
	public String getFileName()
	{
		return this.fileName;
	}
	
	public long getDuration()
	{
		return this.duration;
	}
	
	/**
	 * 组合录音文件名，涵盖文件路径
	 * @return
	 */
	private String getFilePath()
	{
		if (TGUtil.isEmpty(this.userName) || 0==this.startTime)
		{
			return null;
		}
		
		StringBuffer fileName = new StringBuffer();
		fileName.append(TGUtil.getFilePathProperty("audioPath"));
		fileName.append("/").append(String.valueOf(this.currentStartTime)).append(".3gp");
		
		return fileName.toString();
	}
	
	private void deleteCurrentFile()
	{
		if (!TGUtil.isEmpty(this.fileName))
		{
			File file = new File(this.fileName);
			try {
				if (file.exists())
				{
					file.delete();
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			this.fileName = null;
			this.duration = 0;
			this.currentStartTime = 0;
		}
	}
	
	/**
	 * 是否已达到最大允许录制的时长
	 * @return
	 */
	public boolean isMaxDuration()
	{
		if (this.duration >= MAX_DURATION)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
