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

import java.util.List;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PostList extends Fragment {
	private ListView lv;
	protected LayoutInflater inflater;
	Fid app;
	public PostList() {
	}
	private class Controls{
		TextView mTitolo,mRiassunto,mData,mAutore;
		View mMain;
		public Controls(View view) {
			mMain = view;
			mTitolo = (TextView) view.findViewById(R.id.titolo);
		    mRiassunto = (TextView) view.findViewById(R.id.riassunto);
		    mAutore = (TextView) view.findViewById(R.id.autore);
		    mData = (TextView) view.findViewById(R.id.date);
		}
		public void set(final Post item) {
			mTitolo.setText(Html.fromHtml(item.getTitolo()));
			mRiassunto.setText(Html.fromHtml(item.getRiassunto()));
			mAutore.setText(Html.fromHtml(item.getAutore()));
			mData.setText(item.getPubDateStr());
			mMain.setOnClickListener(new OnClickListener() {
				final String link = item.getLink();
				public void onClick(View v) {
					Intent i = new Intent(PostList.this.getActivity(),PostView.class);
					i.putExtra(PostView.POSTLINK, link);
					startActivity(i);
				}
			});
		}
		
	}

	private class Listad extends BaseAdapter{
		List<Post> posts;
		public Listad(final List<Post> posts){
			this.posts = posts;
		}
		public int getCount() {
			return posts.size();
		}

		public Post getItem(int position) {
			return posts.get(position);
		}

		public long getItemId(int position) {
			return getItem(position).getId();
		}

		public View getView(int position, View view, ViewGroup parent) {
			Controls contr;
			if (view==null){
				view = PostList.this.inflater.inflate(R.layout.itempost, parent,false);
				contr = new Controls(view);
				view.setTag(contr);
			} else
				contr = (Controls) view.getTag();
			contr.set(getItem(position));
			return view;
		}
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater = inflater;
		app=(Fid) getActivity().getApplication();
		lv = (ListView) inflater.inflate(R.layout.postlist, container, false);
		lv.setAdapter(new Listad(app.getNews()));
		return lv;
	}
	public void setNews(List<Post> posts) {
		lv.setAdapter(new Listad(posts));
	}	

}
