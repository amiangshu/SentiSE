package edu.sentise.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DbConnector {
	public Connection connect;

	public DbConnector(String databasename) {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(
					"jdbc:mysql://131.230.166.229/" + databasename + "?" + "user=bosu&password=Nice2Hear");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}

	public ArrayList<ReviewComment> getInlineComments() {

		PreparedStatement statement;

		ArrayList<ReviewComment> commentList = new ArrayList<ReviewComment>();
		try {
			statement = connect
					.prepareStatement("select comment_id, message from inline_comments where sentiment_score is null ");

			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				ReviewComment comment = new ReviewComment(rs.getString(1), rs.getString(2), 0);
				commentList.add(comment);


			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return commentList;
	}

	public ArrayList<ReviewComment> getReviewComments(String excludeAuthor) {

		PreparedStatement statement;

		ArrayList<ReviewComment> commentList = new ArrayList<ReviewComment>();
		try {
			statement = connect.prepareStatement("select comments_id, message from review_comments " + excludeAuthor);

			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				ReviewComment comment = new ReviewComment(rs.getString(1), rs.getString(2), 0);
				commentList.add(comment);

			}

		} catch (Exception ex) {
		}
		return commentList;
	}

	public synchronized boolean saveInlineSentiment(ReviewComment comment) {
		try {
			PreparedStatement statement = connect
					.prepareStatement("update inline_comments set sentiment_score=? where comment_id=? ");

			statement.setInt(1, comment.getSentiment_score());
			statement.setString(2, comment.getComment_id());

			statement.executeUpdate();
			//System.out.println("Saved "+comment.getComment_id());
			return true;
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public boolean saveReviewSentiment(ReviewComment comment) {
		try {
			PreparedStatement statement = connect
					.prepareStatement("update review_comments set sentiment_score=? where comments_id=? ");

			statement.setInt(1, comment.getSentiment_score());
			statement.setString(2, comment.getComment_id());

			statement.executeUpdate();

			return true;
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

}