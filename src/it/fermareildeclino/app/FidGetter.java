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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;

public class FidGetter implements ImageGetter {
	Context context;
	Resources res;

	public static class Image {
		public String key, url, alt;
		public byte[] data;
		public Boolean cacheable;
		private static HashMap<String, BitmapDrawable> memcache = new HashMap<String, BitmapDrawable>();
		private final SQLiteDatabase db = Fid.getDb().getWrdb();
		private static final String EMPTYKEY = "EMPTY IMAGE";
		{
			memcache.put(EMPTYKEY, bdfromid(android.R.drawable.ic_delete));
		}

		BitmapDrawable bdfromid(int id) {
			Resources res = Fid.get().getResources();
			Bitmap bm = BitmapFactory.decodeResource(res, id);
			BitmapDrawable bd = new BitmapDrawable(res, bm);
			return bd;
		}

		void writetoDB() {
			ContentValues values = new ContentValues();
			values.put("url", key);
			values.put("alt", alt);
			values.put("dat", data);
			db.insert("images", null, values);
		}

		private boolean fromDB() {
			try {
				Cursor cur = db.rawQuery(
						"select alt,dat from images where url = ?;",
						new String[] { key });
				if (cur != null) {
					cur.moveToNext();
					alt = cur.getString(0);
					data = cur.getBlob(1);
					cacheable = true;
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		}

		public static BitmapDrawable fromurl(String url, Resources res)
				throws IOException {
			Bitmap bm = null;
			Image i = new Image(url, url, "", true);
			if (i.cacheable) {
				if (!i.fromDB()) {// download and cache
					try {
						i.data = Fid.get().getClient().geturl_byte(url);
						i.writetoDB();
					} catch (Exception e) {
						return memcache.get(EMPTYKEY);
					}
				}
			} else {
				i.data = Fid.get().getClient().geturl_byte(url);
			}
			bm = BitmapFactory.decodeByteArray(i.data, 0, i.data.length);
			BitmapDrawable bd = new BitmapDrawable(res, bm);
			return bd;
		}

		private Image(String key, String url, String alt, Boolean cacheable) {
			this.key = key;
			this.url = url;
			this.alt = alt;
			this.data = null;
			this.cacheable = cacheable;
		}
	}

	public FidGetter(Context context) {
		this.context = context;
		res = context.getResources();
	}

	public String formaturl(String url) {
		URL full;
		if (url == null || !url.startsWith("../"))
			return url;
		String newurl = url;
		try {
			full = new URL(url);
			newurl = full.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return newurl;
	}


	public Drawable getDrawable(String url) {
		Drawable d = getImage(formaturl(url));
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		return d;
	}

	public Drawable getImage(String image) {
		if (image.equals(""))
			return null;
		try {
			return Image.fromurl(image, res);
		} catch (IOException e) {
			return null;
		}
	}
}
