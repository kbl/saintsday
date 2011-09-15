/**
 * Sep 15, 2011
 */
package pl.nitroit.saintsday.notification;

import java.util.List;

import pl.nitroit.saintsday.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author kbl
 *
 */
public class ContactArrayAdapter extends ArrayAdapter<Contact> {

	public ContactArrayAdapter(Activity context, List<Contact> contacts) {
		super(context, R.layout.list_item, contacts);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_item, null);

		Contact contact = getItem(position);

		ImageButton call = (ImageButton) rowView.findViewById(R.id.call_icon);
		prepareCallButton(call, contact.getPhoneNumber());

		ImageButton sms = (ImageButton) rowView.findViewById(R.id.sent_message_icon);
		prepareMessageButton(sms, contact.getPhoneNumber());

		TextView display = (TextView) rowView.findViewById(R.id.display_name);
		display.setText(contact.getName());

		return rowView;
	}

	private void prepareMessageButton(ImageButton sms, final String phoneNumber) {
		sms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("address", phoneNumber);
				sendIntent.putExtra("sms_body", getContext().getResources().getString(R.string.sms_body));
				sendIntent.setType("vnd.android-dir/mms-sms");
				getContext().startActivity(sendIntent);
			}
		});
	}

	private void prepareCallButton(ImageButton call, final String phoneNumber) {
		call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + phoneNumber));
				getContext().startActivity(callIntent);
			}
		});
	}

}
