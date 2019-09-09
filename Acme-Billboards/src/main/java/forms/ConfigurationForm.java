package forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

public class ConfigurationForm {

	private String banner;
	private Double finderCacheTime;
	private String defaultPhoneCode;
	private Integer finderQueryResults;
	private String systemName;
	private Double vatPercentage;
	private Double sponsorshipFare;

	private String welcomeTextEnglish;
	private String welcomeTextSpanish;

	@URL
	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	@NotBlank
	public String getDefaultPhoneCode() {
		return defaultPhoneCode;
	}

	public void setDefaultPhoneCode(String defaultPhoneCode) {
		this.defaultPhoneCode = defaultPhoneCode;
	}

	@Range(min = 1, max = 24)
	public Double getFinderCacheTime() {
		return finderCacheTime;
	}

	public void setFinderCacheTime(Double finderCacheTime) {
		this.finderCacheTime = finderCacheTime;
	}

	@NotNull
	@Range(min = 10, max = 100)
	public Integer getFinderQueryResults() {
		return finderQueryResults;
	}

	public void setFinderQueryResults(Integer finderQueryResults) {
		this.finderQueryResults = finderQueryResults;
	}

	@NotBlank
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	@NotNull
	public Double getVatPercentage() {
		return vatPercentage;
	}

	public void setVatPercentage(Double vatPercentage) {
		this.vatPercentage = vatPercentage;
	}

	@NotNull
	@Range(min = 0, max = 1)
	public Double getSponsorshipFare() {
		return sponsorshipFare;
	}

	public void setSponsorshipFare(Double sponsorshipFare) {
		this.sponsorshipFare = sponsorshipFare;
	}

	@NotBlank
	public String getWelcomeTextEnglish() {
		return welcomeTextEnglish;
	}

	public void setWelcomeTextEnglish(String welcomeTextEnglish) {
		this.welcomeTextEnglish = welcomeTextEnglish;
	}

	@NotBlank
	public String getWelcomeTextSpanish() {
		return welcomeTextSpanish;
	}

	public void setWelcomeTextSpanish(String welcomeTextSpanish) {
		this.welcomeTextSpanish = welcomeTextSpanish;
	}

}