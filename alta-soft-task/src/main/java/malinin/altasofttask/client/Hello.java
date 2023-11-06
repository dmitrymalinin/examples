package malinin.altasofttask.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Hello extends Composite {
	interface HelloUiBinder extends UiBinder<Widget, Hello> {}
	private static final HelloUiBinder uiBinder = GWT.create(HelloUiBinder.class);
	
	private static final String helloText = "Hello, Alta-Soft!";	
	private static final DateTimeFormat ruDateTimeFormat = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");

	@UiField
	Label labelHello;
	
	@UiField
	Label labelDate;

	public Hello() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		labelHello.setText(helloText);
		labelDate.setText(ruDateTimeFormat.format(new Date()));
	}
	
	
}
