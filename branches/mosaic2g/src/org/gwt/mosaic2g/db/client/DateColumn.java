package org.gwt.mosaic2g.db.client;

import java.util.Date;

import org.gwt.mosaic2g.binding.client.Binder;
import org.gwt.mosaic2g.binding.client.Converter;
import org.gwt.mosaic2g.binding.client.Property;
import org.gwt.mosaic2g.binding.client.Validator;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;

public class DateColumn extends Column<Date> {

	private final static DateTimeFormat DEFAULT_FORMAT = DateTimeFormat
			.getFormat(PredefinedFormat.DATE_TIME_MEDIUM);

	private DateTimeFormat dateTimeFormat = DEFAULT_FORMAT;

	private final Property<String> displayValue = getValue().createBinding(
			new Converter<Date, String>() {
				@Override
				public String convertForward(Date value) {
					return value == null ? "".intern()
							: dateTimeFormat.format(value);
				}

				@Override
				public Date convertReverse(String value) {
					return dateTimeFormat.parse(value);
				}
			}, new Validator<Date>() {
				@Override
				public Result validate(Date value) {
					// TODO Auto-generated method stub
					return null;
				}
			});

	public DateColumn(String name) {
		super(name);
	}

	public DateColumn(String name, Date value) {
		super(name, value);
	}

	public DateColumn(String name, Binder<Date> binder) {
		super(name, binder);
	}

	public DateTimeFormat getDateTimeFormat() {
		return dateTimeFormat;
	}

	public void setDateTimeFormat(DateTimeFormat dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat == null ? DEFAULT_FORMAT
				: dateTimeFormat;
	}

	@Override
	public Property<String> getDisplayValue() {
		return displayValue;
	}

}
