package ins.marianao.ticketing.fxml;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import cat.institutmarianao.ticketingws.model.Room;
import cat.institutmarianao.ticketingws.model.User;
import ins.marianao.ticketing.fxml.manager.ResourceManager;
import javafx.util.Pair;
import javafx.util.StringConverter;

public class Formatters {
	public static final int CC_NUM_SIZE = 16;
	public static final int EXT_NUM_SIZE = 4;

	public static StringConverter<User> getUserConverter(List<User> users) {
		
		StringConverter<User> converter = new StringConverter<User>()	{

			private User findUser(String username) {
    			if (username == null || "".equals(username)) return null;
    			for (User user : users) {
					if (username.equals(user.getUsername())) return user;
				}
    			return null;
    		}
    		
    		@Override
			public User fromString(String username)
			{
					return findUser(username);
			}

			@Override
			public String toString(User user)
			{
				if (user == null || ResourceManager.getInstance().getText("fxml.text.viewTickets.all").equals(user.getUsername())) return ResourceManager.getInstance().getText("fxml.text.viewTickets.all");
				return user.getFullName();
			}
		};
		return converter;
	}
	
	public static StringConverter<Pair<String,String>> getUserRoleConverter() {
		StringConverter<Pair<String,String>> converter = new StringConverter<Pair<String,String>>()	{
			private ResourceBundle resource;
			{
				this.resource = ResourceManager.getInstance().getTranslationBundle();
			}
			private String findString(String text) {
    			if (text == null || "".equals(text)) return null;
    			for (String key : this.resource.keySet()) {
					if (text.equals(this.resource.getString(key))) return key;
				}
    			return null;
    		}
			
    		@Override
			public Pair<String,String> fromString(String text)
			{
    			String key = findString(text);
    			if (key == null) return null;
    			key = key.replaceAll("text.User.", key);
    			return new Pair<String, String>(key, this.resource.getString("text.User."+key));
			}

			@Override
			public String toString(Pair<String,String> pair)
			{
				String key = pair == null?"ALL":pair.getKey();
				return this.resource.getString("text.User."+key);
			}
		};
		return converter;
	}
	
	public static StringConverter<Room> getRoomConverter(List<Room> rooms) {
		
		StringConverter<Room> converter = new StringConverter<Room>()	{

			private Room findRoom(String name) {
    			if (name == null || "".equals(name)) return null;
    			for (Room room : rooms) {
					if (room != null && name.equals(room.getName())) return room;
				}
    			return new Room(name);
    		}
    		
    		@Override
			public Room fromString(String name)
			{
					return findRoom(name);
			}

			@Override
			public String toString(Room room)
			{
				if (room == null) return null;
				return room.getName();
			}
		};
		return converter;
	}
	
	
	public static StringConverter<Integer> getExtensioFormatter() {
		StringConverter<Integer> formatter = new StringConverter<Integer>()
		{
			@Override
			public Integer fromString(String string)
			{
				string = string.replace(" ", "");
				NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("CA","ES"));
				int value;
				try {
					value = nf.parse(string).intValue();
				} catch (ParseException e) {
					//e.printStackTrace();
					value = 0;
				}
				return value;
			}

			@Override
			public String toString(Integer object)
			{
				if (object == null) return "N/F";

				return StringUtils.leftPad(String.valueOf(object), EXT_NUM_SIZE, "0").replaceFirst("(\\d{2})(\\d+)", "$1 $2");
			}
		};
		return formatter;
	}

	public static StringConverter<Long> getCreditCardFormatter() {
		StringConverter<Long> formatter = new StringConverter<Long>()
		{
			@Override
			public Long fromString(String string)
			{
				string = string.replace(" ", "");
				NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("CA","ES"));
				Long value;
				try {
					value = nf.parse(string).longValue();
				} catch (ParseException e) {
					//e.printStackTrace();
					value = 0L;
				}
				return value;
			}

			@Override
			public String toString(Long object)
			{
				if (object == null) return "N/F";

				return StringUtils.leftPad(String.valueOf(object), CC_NUM_SIZE, "0").replaceFirst("(\\d{4})(\\d{4})(\\d{4})(\\d+)", "$1 $2 $3 $4");
			}
		};
		return formatter;
	}

	public static StringConverter<LocalDate> getCreditCardDateFormatter(String pattern) {
		DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern(pattern);

		StringConverter<LocalDate> formatter = new StringConverter<LocalDate>()
		{
			@Override
			public LocalDate fromString(String string)
			{
				string = string.replace(" ", "");

				return LocalDateTime.parse(string, dtFormatter).toLocalDate();
			}

			@Override
			public String toString(LocalDate object)
			{
				if (object == null) return "N/F";

				return object.format(dtFormatter);
			}
		};
		return formatter;
	}

	public static StringConverter<Integer> getIntegerFormatter() {
		NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("CA","ES"));

		StringConverter<Integer> formatter = new StringConverter<Integer>()
		{
			@Override
			public Integer fromString(String string)
			{
				Integer value;
				try {
					value = nf.parse(string).intValue();
				} catch (ParseException e) {
					//e.printStackTrace();
					value = 0;
				}
				return value;
			}

			@Override
			public String toString(Integer object)
			{
				return (object == null)?nf.format(0):nf.format(object);
			}
		};
		return formatter;
	}

	public static StringConverter<Double> getDecimalFormatter() {
		DecimalFormat df = new DecimalFormat( "#0.0" );

		StringConverter<Double> formatter = new StringConverter<Double>()
		{
			@Override
			public Double fromString(String string)
			{
				Double value;
				try {
					//value = nf.parse(string).doubleValue();
					value = df.parse(string).doubleValue();
				} catch (ParseException e) {
					//e.printStackTrace();
					value = 0.0;
				}
				return value;
			}

			@Override
			public String toString(Double object)
			{
				return (object == null)?df.format(0.0):df.format(object);
			}
		};
		return formatter;
	}

	public static StringConverter<Double> getModedaFormatter() {
		StringConverter<Double> formatter = new StringConverter<Double>()
		{
			@Override
			public Double fromString(String string)
			{
				NumberFormat nf = NumberFormat.getInstance(new Locale("es","CA"));

				Double value;
				try {
					value = nf.parse(string).doubleValue();
				} catch (ParseException e) {
					value = 0.0;
				}
				return value;
			}

			@Override
			public String toString(Double object)
			{
				DecimalFormat df = new DecimalFormat( "###,##0.00€" );

				return (object == null)?df.format(0.0):df.format(object);
			}
		};
		return formatter;
	}
}
