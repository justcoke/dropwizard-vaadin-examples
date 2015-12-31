package org.bonn.se.model.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bonn.se.gui.ui.MyUI;
import org.bonn.se.model.objects.dto.User;
import org.bonn.se.model.objects.entities.Booking;

import com.vaadin.ui.UI;

public class BuchungDAO extends AbstractDAO {
	private static BuchungDAO dao = null;

	public static BuchungDAO getInstance() {
		if (dao == null) {
			dao = new BuchungDAO();
		}
		return dao;
	}

	private BuchungDAO() {
	}

	public boolean addBooking(Booking booking) {
		String sql = "INSERT INTO booking VALUES (default, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement preparedStatement = getPreparedStatement(sql);

		User user = ((MyUI) UI.getCurrent()).getUser();

		try {
			preparedStatement.setDate(1, new Date(booking.getAnreise().getTime()));
			preparedStatement.setDate(2, new Date(booking.getAbreise().getTime()));
			preparedStatement.setString(3, booking.getIban());
			preparedStatement.setInt(4, booking.getNumber());
			preparedStatement.setString(5, booking.getUser().getLogin());
			preparedStatement.setDate(6, new Date(booking.getDatumBuchung().getTime()));
			preparedStatement.setInt(7, booking.getHotel().getId());

			preparedStatement.executeUpdate();

			setBuchungsId(booking);

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void deleteBookingBy(int id) {
		try {
			getStatement().executeQuery("DELETE FROM booking WHERE id = " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void setBuchungsId(Booking booking) {
		Statement statement = getStatement();
		ResultSet resultSet = null;

		try {
			resultSet = statement.executeQuery("SELECT max(id) FROM booking");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int currentValue = 0;
		try {
			resultSet.next();
			currentValue = resultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		booking.setId(currentValue);
	}
}
