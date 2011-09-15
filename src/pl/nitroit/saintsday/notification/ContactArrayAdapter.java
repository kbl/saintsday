/**
 * Sep 15, 2011
 */
package pl.nitroit.saintsday.notification;

import java.util.List;

import pl.nitroit.saintsday.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author kbl
 *
 */
public class ContactArrayAdapter extends ArrayAdapter<Contact> {

	private Activity context;

	public ContactArrayAdapter(Activity context, List<Contact> contacts) {
		super(context, R.layout.list_item, contacts);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_item, null);

		Contact contact = getItem(position);

		ImageView avatar = (ImageView) rowView.findViewById(R.id.avatar);
		avatar.setImageResource(android.R.drawable.ic_menu_manage);

		ImageButton call = (ImageButton) rowView.findViewById(R.id.call_icon);
		call.setImageResource(android.R.drawable.ic_menu_call);

		ImageButton sms = (ImageButton) rowView.findViewById(R.id.sent_message_icon);
		sms.setImageResource(android.R.drawable.ic_menu_send);

		TextView display = (TextView) rowView.findViewById(R.id.display_name);
		display.setText(contact.getName());

		return rowView;
	}

}
