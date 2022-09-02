package fr.kinjer.oldforge.bot.command.model.gson;

import com.google.gson.annotations.SerializedName;

/**
 * ForgeEvents Class used to retrieve information about events
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class ForgeEvents {

	@SerializedName("package")
	private String packageName;
	private String description;
	private String anchors;

	/**
	 * @return the packageName
	 */
	public String getPackage() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackage(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the anchors
	 */
	public String getAnchors() {
		return anchors;
	}

	/**
	 * @param anchors the anchors to set
	 */
	public void setAnchors(String anchors) {
		this.anchors = anchors;
	}
}
