package org.nearbyshops.DAOPreparedSettings;


import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.ModelSettings.Settings;

import java.sql.*;


public class SettingsDAOPrepared {


	private HikariDataSource dataSource = Globals.getDataSource();


	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();	
	}


	public int saveSettings(Settings settings)
	{

		Connection connection = null;
		PreparedStatement statement = null;
		int rowIdOfInsertedRow = -1;

		String insertItemCategory = "INSERT INTO "
				+ Settings.TABLE_NAME
				+ "("
				+ Settings.DISTRIBUTOR_ENABLED_DEFAULT + ","
				+ Settings.END_USER_ENABLED_DEFAULT + ","
				+ Settings.GOOGLE_MAPS_API_KEY + ","

				+ Settings.SETTING_CONFIGURATION_ID + ""
				+ " ) VALUES (?,?,? ,?)";
		
		try {
			
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(insertItemCategory,Statement.RETURN_GENERATED_KEYS);

			statement.setObject(1,settings.getDistributorEnabledByDefault());
			statement.setObject(2,settings.getEndUserEnabledByDefault());
			statement.setString(3,settings.getGoogleMapsAPIKey());

			statement.setObject(4,1);

			rowIdOfInsertedRow = statement.executeUpdate();

			ResultSet rs = statement.getGeneratedKeys();

			if(rs.next())
			{
				rowIdOfInsertedRow = rs.getInt(1);
			}

			
			System.out.println("Key autogenerated Save CurrentServiceConfiguration: " + rowIdOfInsertedRow);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			
			try {
			
				if(statement!=null)
				{statement.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				if(connection!=null)
				{connection.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		return rowIdOfInsertedRow;
	}




	public int updateSettings(Settings settings)
	{

		// Ensure the service configuration row exist before being updated
		if(getServiceConfiguration()==null){
			saveSettings(getDefaultConfiguration());
		}


		String updateStatement = "UPDATE " + Settings.TABLE_NAME

				+ " SET "

				+ Settings.DISTRIBUTOR_ENABLED_DEFAULT + " = ?,"
				+ Settings.END_USER_ENABLED_DEFAULT + " = ?,"
				+ Settings.GOOGLE_MAPS_API_KEY + " = ?"

				+ " WHERE "
				+ Settings.SETTING_CONFIGURATION_ID + " = ?";


		Connection connection = null;
		PreparedStatement statement = null;
		int updatedRows = -1;
		
		try {
			
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(updateStatement);

			statement.setObject(1,settings.getDistributorEnabledByDefault());
			statement.setObject(2,settings.getEndUserEnabledByDefault());
			statement.setString(3,settings.getGoogleMapsAPIKey());
			statement.setObject(4,1);

			updatedRows = statement.executeUpdate();

			System.out.println("Total rows updated: " + updatedRows);
			
			//conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		
		{
			
			try {
			
				if(statement!=null)
				{statement.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				if(connection!=null)
				{connection.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return updatedRows;
		
	}




	public int deleteSettings()
	{
		
		String deleteStatement = "DELETE FROM " + Settings.TABLE_NAME
				+ " WHERE " + Settings.SETTING_CONFIGURATION_ID + " = ?";
		
		
		Connection connection= null;
		PreparedStatement statement = null;
		int rowsCountDeleted = 0;
		try {
			
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(deleteStatement);

			statement.setInt(1,1);
			rowsCountDeleted = statement.executeUpdate();
			System.out.println(" Deleted Count: " + rowsCountDeleted);

			connection.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally
		
		{
			
			try {
			
				if(statement!=null)
				{statement.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				if(connection!=null)
				{connection.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
		return rowsCountDeleted;
	}




	public Settings getServiceConfiguration()
	{
		
		String query = "SELECT * FROM " + Settings.TABLE_NAME
						+ " WHERE " + Settings.SETTING_CONFIGURATION_ID + " = " + 1;
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		Settings settings = null;

		try {
			
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(query);
			
			while(rs.next())
			{

				settings = new Settings();

				settings.setGoogleMapsAPIKey(rs.getString(Settings.GOOGLE_MAPS_API_KEY));
				settings.setDistributorEnabledByDefault(rs.getBoolean(Settings.DISTRIBUTOR_ENABLED_DEFAULT));
				settings.setEndUserEnabledByDefault(rs.getBoolean(Settings.END_USER_ENABLED_DEFAULT));
				settings.setSettingsID(rs.getInt(Settings.SETTING_CONFIGURATION_ID));

			}

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		
		{
			
			try {
					if(rs!=null)
					{rs.close();}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			try {
			
				if(statement!=null)
				{statement.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				if(connection!=null)
				{connection.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(settings == null)
		{
			saveSettings(getDefaultConfiguration());
		}
	
		return settings;
	}



	public Settings getDefaultConfiguration()
	{
		Settings settings = new Settings();

		settings.setEndUserEnabledByDefault(true);
		settings.setDistributorEnabledByDefault(false);
		settings.setGoogleMapsAPIKey("API-KEY-NOT-SET");

		return  settings;
	}



}