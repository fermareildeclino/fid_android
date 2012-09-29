/************************************************************************************
 * This code is part of Fermare il declino for android                              *
 * Copyright © 2012 ALI - Associazione Lavoro e Impresa per le Liberta' Economiche  *
 *   http://www.fermareildeclino.it info@fermareildeclino.it                        *
 *                                                                                  *
 * This program is free software; you can redistribute it and/or                    *
 * modify it under the terms of the GNU General Public License                      *
 * as published by the Free Software Foundation; either version 2                   *
 * of the License, or (at your option) any later version.                           *
 *                                                                                  *
 * This program is distributed in the hope that it will be useful,                  *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                   *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                    *
 * GNU General Public License for more details.                                     *
 *                                                                                  *
 * You should have received a copy of the GNU General Public License                *
 * along with this program; if not, write to the Free Software                      *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.  *
 ************************************************************************************/ 
package it.fermareildeclino.app;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

@SuppressLint("ParserError")
public class Downloader extends IntentService {
	public static final String FID_URL       = "FID_URL",
			                   FID_FEEDTYPE  = "FID_FEEDTYPE",
			                   FID_MESSENGER ="FID_MESS";
	public static final byte FID_MAINRSS = 1, FID_VIDEORSS = 2;
	private static final String FID = "FermareIlDeclino";
	private List<Post> posts;

	public Downloader() {
		super("Downloader");
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		this.posts= null;
		Bundle extras = intent.getExtras();
		if (extras==null) return;
		final String urlPath = intent.getStringExtra(FID_URL);
		intent.getByteExtra(FID_FEEDTYPE, FID_MAINRSS);
		try {
			switch (intent.getByteExtra(FID_FEEDTYPE, FID_MAINRSS)) {
			case FID_MAINRSS:
				posts = Post.decodeFeed( Fid.get().getClient().geturl_doc(urlPath));
				((Fid)getApplication()).setNews(posts);
				Post.savenew(posts);
				startPostDownload(posts);
			case 2:
			default:
			}
		} catch (IOException e) {
			Log.w(FID, "IO", e);
		} catch (ParserConfigurationException e) {
			Log.w(FID, "ParserCfg", e);
		} catch (SAXException e) {
			Log.w(FID, "SAX", e);
		}
		Messenger m = (Messenger) extras.get(FID_MESSENGER);
		Message msg = Message.obtain();
		msg.arg1 = this.posts==null?Activity.RESULT_CANCELED:Activity.RESULT_OK;
		msg.obj=this.posts;
		try {
			m.send(msg);
		} catch (RemoteException e) {
			Log.w(FID, "SAX", e);
		}
	}

	private void startPostDownload(final List<Post> posts) {
		new Thread(){
			@Override
			public void run() {
				for(Post post :posts){
					if(post.getTesto()==null ||post.getTesto().equals(""))
						try {
							post.download();
						} catch (IOException e) {
							Log.w("Fid",e);
						}
				}
			}
		}.start();
	}

}
