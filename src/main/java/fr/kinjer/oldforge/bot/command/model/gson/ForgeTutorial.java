package fr.kinjer.oldforge.bot.command.model.gson;

/**
 * ForgeTutorial Class used to retrieve information about tutorial
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.3
 * @param <T>
 * @since 1.0.3
 */
public class ForgeTutorial {

	private String title;
	private String url;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
