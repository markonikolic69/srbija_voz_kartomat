package application.fiskalizacija;


public class OptionsBean {
	private String omitQRCodeGen = "0";
	private String omitTextualRepresentation = "0";
	public String getOmitQRCodeGen() {
		return omitQRCodeGen;
	}
	public void setOmitQRCodeGen(String omitQRCodeGen) {
		this.omitQRCodeGen = omitQRCodeGen;
	}
	public String getOmitTextualRepresentation() {
		return omitTextualRepresentation;
	}
	public void setOmitTextualRepresentation(String omitTextualRepresentation) {
		this.omitTextualRepresentation = omitTextualRepresentation;
	}
	@Override
	public String toString() {
		return "OptionsBean [omitQRCodeGen=" + omitQRCodeGen + ", omitTextualRepresentation="
				+ omitTextualRepresentation + "]";
	}
	
	
	
}
