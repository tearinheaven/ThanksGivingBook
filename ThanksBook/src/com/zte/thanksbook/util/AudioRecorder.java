package com.zte.thanksbook.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

import android.media.MediaRecorder;
import android.util.Log;

public class AudioRecorder {
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
	
	public AudioRecorder(String userName) {
		this.userName = userName;
		this.startTime = Long.valueOf(new Date().getTime());;
		this.fileName = null;
		this.lastFileName = null;
		this.recorder = null;
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

	        this.recorder.start();
		}
		else
		{
			Log.e("ThanksBook", "¼���ļ�·����ȡʧ�ܣ�");
		}
	}
	
	/**
	 * ����¼��
	 */
	public void stopRecording()
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
}
