package eu.apenet.dashboard.indexing;


public class LevelInfo {
	private Long clId;
	private Integer hgOrFaId;
	private String unittitle;
	private Integer orderId = 0;
	public LevelInfo(Integer hgOrFaId){
		this.hgOrFaId = hgOrFaId;
	}

	public LevelInfo(Long clId, Integer orderId, String unittitle){
		this.clId = clId;
		this.orderId = orderId;
		this.unittitle = unittitle;
	}
	public Long getClId() {
		return clId;
	}
	public String getUnittitle() {
		return unittitle;
	}
	public Integer getOrderId() {
		return orderId;
	}

	public Integer getHgOrFaId() {
		return hgOrFaId;
	}

}
