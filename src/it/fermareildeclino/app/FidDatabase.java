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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FidDatabase {
	private static FidDatabase _inst;
	private Helper h;

	private class Helper extends SQLiteOpenHelper {
		static final String dbname = "restsearch_db";
		static final int version = 1;

		public Helper(Context context) {
			super(context, dbname, null, version);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL("CREATE TABLE IF NOT EXISTS images (url text primary key,"
					+ " alt TEXT not null," + " dat BLOB);");
			database.execSQL("CREATE TABLE IF NOT EXISTS posts ("
					+ "id         integer primary key autoincrement,"
					+ "pubdate    integer,"
					+ "titolo     text," 
					+ "riassunto  text,"
					+ "autore     text   ," 
					+ "link       text   ," 
					+ "letto      integer," 
					+ "nuovo      integer,"
					+ "testo      text   ,"
					+ "source     text );");
			database.execSQL("CREATE TABLE IF NOT EXISTS categories ("
					+ " domain text not null," 
					+ " label  text not null, primary key (domain));");
			database.execSQL("CREATE TABLE IF NOT EXISTS postcat ("
					+ " postid integer,"  
					+ " domain text,"
					+ " primary key (postid,domain));");
			database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS postlink ON posts (link)");
			database.execSQL("CREATE INDEX IF NOT EXISTS postdate ON posts (pubdate)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int version1,
				int version2) {
			onCreate(database);
		}
	}

	static public FidDatabase get(Context cont) {
		if (_inst == null) {
			_inst = new FidDatabase(cont);
		}
		return _inst;
	}

	static public FidDatabase get() {
		return _inst;
	}

	private FidDatabase(Context cont) {
		h = new Helper(cont);
	}

	public SQLiteDatabase getWrdb() {
		return _inst.h.getWritableDatabase();
	}

	public SQLiteDatabase getRodb() {
		return _inst.h.getReadableDatabase();
	}
}
