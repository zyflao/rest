/**
 * 
 */
package test;


import com.jrj.common.page.PageInfo;

/**  
 *   
 *   
 * @history  
 * <PRE>  
 * ---------------------------------------------------------  
 * VERSION       DATE            BY       CHANGE/COMMENT  
 * ---------------------------------------------------------  
 * 1.0           2012-4-27    yuanlong.wang     create  
 * ---------------------------------------------------------  
 * </PRE>  
 *  
 */

public class Test {
	public static void main(String[] args){
		PageInfo p1 = new PageInfo();
		p1.setTotalCount(132);
		System.out.println(p1.makePage("http://_pn=", 2));		
	}
}
