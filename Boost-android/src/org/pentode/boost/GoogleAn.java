package org.pentode.boost;

import org.pentode.boost.Boost.Analytics;

import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

public class GoogleAn implements Analytics {
	private final Tracker tracker;

	public GoogleAn(Tracker tracker) {
		this.tracker = tracker; 
	}
	
	@Override
	public void win(int level) {
	  // May return null if a EasyTracker has not yet been initialized with a
	  // property ID.
	  //EasyTracker easyTracker = EasyTracker.getInstance(this);

	  // MapBuilder.createEvent().build() returns a Map of event fields and values
	  // that are set and sent with the hit.
	  tracker.send(MapBuilder
	      .createEvent("ui_action",     // Event category (required)
	                   "button_press",  // Event action (required)
	                   Integer.toString(level),   // Event label
	                   null)            // Event value
	      .build()
	  );
	}
}
