package com.project.imageCharacteristics;

public class ImageCharacteristics {
	private String focalLength;
	private String fNumber;
	private String exposureTime;
	private String isoSpeedRatings;
	private String dateTaken;
	
	public ImageCharacteristics(String focalLength, String fNumber, String exposureTime, String isoSpeedRatings,
			String dateTaken) {
		setFocalLength(focalLength);
		setfNumber(fNumber);
		setExposureTime(exposureTime);
		setIsoSpeedRatings(isoSpeedRatings);
		setDateTaken(dateTaken);
	}

	public String getFocalLength() {
		return focalLength;
	}
	
	public String getfNumber() {
		return fNumber;
	}
	
	public String getExposureTime() {
		return exposureTime;
	}
	
	public String getIsoSpeedRatings() {
		return isoSpeedRatings;
	}
	
	public String getDateTaken() {
		return dateTaken;
	}

	private void setFocalLength(String focalLength) {
		this.focalLength = focalLength == null ? "" : focalLength;
	}

	private void setfNumber(String fNumber) {
		this.fNumber = fNumber == null ? "" : fNumber;
	}

	private void setExposureTime(String exposureTime) {
		this.exposureTime = exposureTime == null ? "" : exposureTime;
	}

	private void setIsoSpeedRatings(String isoSpeedRatings) {
		this.isoSpeedRatings = isoSpeedRatings == null ? "" : isoSpeedRatings;
	}

	private void setDateTaken(String dateTaken) {
		this.dateTaken = dateTaken == null ? "" : dateTaken;
	}

	@Override
	public String toString() {
		return "ImageCharacteristics [focalLength=" + focalLength + ", fNumber=" + fNumber + ", exposureTime="
				+ exposureTime + ", isoSpeedRatings=" + isoSpeedRatings + ", dateTaken=" + dateTaken + "]";
	}
	
	
}
