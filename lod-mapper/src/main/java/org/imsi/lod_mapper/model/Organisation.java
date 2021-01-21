package org.imsi.lod_mapper.model;

import java.io.Serializable;
import java.util.List;

public class Organisation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2443002089568128681L;
	private String id;
	private List<String> originalid;
	private List<String> legalname;
	private List<String> legalshortname;
	private List<String> alternativenames;
	private List<String> country;
	private List<String> dateofcollection;
	private List<String> dateoftransformation;
	private List<String> collectedfrom;
	private List<String> pid;
	private List<String> websiteurl;
	private List<String> logourl;
	private List<String> target;
	private List<String> reltype;
	private List<String> subreltype;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getOriginalid() {
		return originalid;
	}
	public void setOriginalid(List<String> originalid) {
		this.originalid = originalid;
	}
	public List<String> getLegalname() {
		return legalname;
	}
	public void setLegalname(List<String> legalname) {
		this.legalname = legalname;
	}
	public List<String> getLegalshortname() {
		return legalshortname;
	}
	public void setLegalshortname(List<String> legalshortname) {
		this.legalshortname = legalshortname;
	}
	public List<String> getAlternativenames() {
		return alternativenames;
	}
	public void setAlternativenames(List<String> alternativenames) {
		this.alternativenames = alternativenames;
	}
	public List<String> getCountry() {
		return country;
	}
	public void setCountry(List<String> country) {
		this.country = country;
	}
	public List<String> getDateofcollection() {
		return dateofcollection;
	}
	public void setDateofcollection(List<String> dateofcollection) {
		this.dateofcollection = dateofcollection;
	}
	public List<String> getDateoftransformation() {
		return dateoftransformation;
	}
	public void setDateoftransformation(List<String> dateoftransformation) {
		this.dateoftransformation = dateoftransformation;
	}
	public List<String> getCollectedfrom() {
		return collectedfrom;
	}
	public void setCollectedfrom(List<String> collectedfrom) {
		this.collectedfrom = collectedfrom;
	}
	public List<String> getPid() {
		return pid;
	}
	public void setPid(List<String> pid) {
		this.pid = pid;
	}
	public List<String> getWebsiteurl() {
		return websiteurl;
	}
	public void setWebsiteurl(List<String> websiteurl) {
		this.websiteurl = websiteurl;
	}
	public List<String> getLogourl() {
		return logourl;
	}
	public void setLogourl(List<String> logourl) {
		this.logourl = logourl;
	}
	public List<String> getTarget() {
		return target;
	}
	public void setTarget(List<String> target) {
		this.target = target;
	}
	public List<String> getReltype() {
		return reltype;
	}
	public void setReltype(List<String> reltype) {
		this.reltype = reltype;
	}
	public List<String> getSubreltype() {
		return subreltype;
	}
	public void setSubreltype(List<String> subreltype) {
		this.subreltype = subreltype;
	}
	
	public List<String> get(int pos) {
		switch(pos) {
			case 1:
				return originalid;
			case 2:
				return legalname;
			case 3:
				return legalshortname;
			case 4:
				return alternativenames;
			case 5:
				return country;
			case 6:
				return dateofcollection;
			case 7:
				return dateoftransformation;
			case 8:
				return collectedfrom;
			case 9:
				return pid;
			case 10:
				return websiteurl;
			case 11:
				return logourl;
			case 12:
				return target;
			case 13:
				return reltype;
			case 14:
				return subreltype;
			default:
				return null;
		}
	}
	@Override
	public String toString() {
		return "Organisation [id=" + id + ", originalid=" + originalid + ", legalname=" + legalname
				+ ", legalshortname=" + legalshortname + ", alternativenames=" + alternativenames + ", country="
				+ country + ", dateofcollection=" + dateofcollection + ", dateoftransformation=" + dateoftransformation
				+ ", collectedfrom=" + collectedfrom + ", pid=" + pid + ", websiteurl=" + websiteurl + ", logourl="
				+ logourl + ", target=" + target + ", reltype=" + reltype + ", subreltype=" + subreltype + "]";
	}



}
