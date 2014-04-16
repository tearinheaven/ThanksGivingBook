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
	 * ���¼��ʱ��
	 */
	private static final long MAX_DURATION = 299900;
	
	/**
	 * ��ǰ�û���
	 */
	private String userName;
	
	/**
	 * ��ʼʱ�� 
	 */
	private long startTime;
	
	/**
	 * ��ǰ¼����ʼʱ��
	 */
	private long currentStartTime;
	
	/**
	 * ��ǰ¼���ļ���
	 */
	private String fileName;
	
	/**
	 * ¼��ʱ�� ms
	 */
	private long duration;
	
	/**
	 * ��һ��¼���ļ�����
	 */
	private String lastFileName;
	
	/**
	 * ��һ��¼����ʱ�� ms
	 */
	private long lastDuration;
	
	/**
	 * ¼��ʵ��
	 */
	private MediaRecorder recorder = null;
	
	/**
	 * ����ʵ��
	 */
	private AudioRecorderListener listener;
	
	//private RecordTask recordTask = null;
	
	private boolean isRecording;
	
	/**
	 * ¼��������
	 */
	public interface AudioRecorderListener{
		//����¼��
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
	 * ��ʼ¼��
	 */
	public void startRecording()
	{		
		//�����ǰ�ļ�����Ϊ�գ���Ϊ�ϵ���¼
		if (!TGUtil.isEmpty(this.fileName))
		{
			this.lastFileName = this.fileName;
			this.lastDuration = this.duration;
		}

		this.currentStartTime = Long.valueOf(new Date().getTime());
		this.fileName = getFilePath();
		if (!TGUtil.isEmpty(this.fileName))
		{
			//��ʼ¼��
	        this.recorder = new MediaRecorder();
	        this.recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        this.recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);	        
	        this.recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	        Log.v("ThanksBook", "¼���ļ�·����" + this.fileName);
	        this.recorder.setOutputFile(this.fileName);

	        try {
	        	this.recorder.prepare();
	        } catch (IOException e) {
	            Log.e("ThanksBook", "¼��ʵ�� prepare() ʧ��");
	            e.printStackTrace();
	        }

        	/*this.recordTask = null;
        	this.recordTask = new RecordTask();
	        this.recordTask.execute(new Void[]{});*/
	        
	        new RecordTask().execute(new Void[]{});
	        
	        //this.recorder.start();
	        
	        //������̨����¼�����ȵ��߳�
	        //new RecordTimeControlTask().execute(new Void[]{});
		}
		else
		{
			Log.e("ThanksBook", "¼���ļ�·����ȡʧ�ܣ�");
		}
	}
	
	/**
	 * ����¼��
	 */
	public synchronized void stopRecording()
	{
		if (this.isRecording)
		{
			Log.v("ThanksBook", "����¼��");
			this.isRecording = false;	
		}
	}
	
	/**
	 * ����¼��
	 */
	public synchronized void doStopRecording()
	{
		if (this.recorder!=null)
		{
			this.recorder.stop();
			long endTime = Long.valueOf(new Date().getTime());
			this.duration = endTime - this.currentStartTime;
			Log.v("ThanksBoos", "¼��ʱ����" + this.duration);
			this.recorder.release();
			this.recorder = null;
			
			//����Ƕϵ���¼��������ļ�
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
		            
		            //ɾ���ɵ������ļ�
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
			
			Log.v("ThanksBook", "¼�����̿�ʼ");
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

			Log.v("ThanksBook", "¼�����̼�⵽¼����ֹ");
			doStopRecording();
			return null;
		}
		
		@Override  
        protected void onPostExecute(Void result) { 		
			if (null != listener)
			{
				Log.v("ThanksBook", "¼�����̽���������UI");
				listener.onRecorderStop();
			}
        }
	};
	
	/*private class RecordTimeControlTask extends AsyncTask<Void, Integer, Void> {
		//����ʱ��Ϊ60��
		@Override
		protected Void doInBackground(Void... params) {
			Log.v("ThanksBook", "¼�����ƽ��̿�ʼ");
			
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
						//Log.v("ThanksBook", "¼��ʱ����" + thisDuration);
					}
				};
			} catch (Exception ex)
			{
				Log.e("ThanksBook", "¼�����ƽ���ʧ��");
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
	 * ��ȡ¼���ļ���
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
	 * ���¼���ļ����������ļ�·��
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
	 * �Ƿ��Ѵﵽ�������¼�Ƶ�ʱ��
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
