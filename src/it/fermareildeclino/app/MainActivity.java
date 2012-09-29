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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	public PostList news;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message message) {
			@SuppressWarnings("unchecked")
			List<Post> posts = (List<Post>) message.obj;
			if (message.arg1 == RESULT_OK && posts != null) {
				Log.w("FID", "Posts received");
				news.setNews(posts);

			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		private static final int NUM_FRAMES = 3;
		private Fragment[] fragments = new Fragment[NUM_FRAMES];

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Bundle args = new Bundle();
			Fragment f;
			if(i<NUM_FRAMES&&fragments[i]!=null) return fragments[i];
			switch (i) {
			case 0:
				f=fragments[i] = news = new PostList();
				fragments[i].setArguments(args);
				refresh();
				break;
			case 1:
				args.putString(HtmlSectionFragment.ARG_URL,
						"file:///android_asset/FidManifest.html");
				f=fragments[i] = new HtmlSectionFragment();
				fragments[i].setArguments(args);
				break;
			case 2:
				f=fragments[i] = new Punti();
				f.setArguments(args);
				refresh();
				break;				
			default:
				f = new DummySectionFragment();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
				f.setArguments(args);
				break;
			}
			return f;
		}

		@Override
		public int getCount() {
			return NUM_FRAMES;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase();
			case 1:
				return getString(R.string.title_section2).toUpperCase();
			case 2:
				return getString(R.string.title_section3).toUpperCase();
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		public DummySectionFragment() {
		}

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			Bundle args = getArguments();
			textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
			return textView;
		}
	}

	public static class HtmlSectionFragment extends Fragment {
		public HtmlSectionFragment() {
		}

		public static final String ARG_URL = "URL";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			WebView webView = new WebView(getActivity());
			Bundle args = getArguments();
			webView.loadUrl(args.getString(ARG_URL));
			return webView;
		}
	}

	public void refresh() {
		Intent intent = new Intent(this, Downloader.class);
		Messenger messenger = new Messenger(handler);
		intent.putExtra(Downloader.FID_MESSENGER, messenger);
		intent.putExtra(Downloader.FID_URL,
				"http://www.fermareildeclino.it/rss.xml");
		startService(intent);
	}
}
