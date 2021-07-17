package tonyu.kernel;

public class TonyuProjectManager {
	public void loadPage(TonyuPage loadPage) {
		TGL.tonyuBoot.movePage(loadPage);
	}
	public TonyuPage getCurrentPageName() {
		return TGL.tonyuBoot.getCurrentPage();
	}

}
