package henson.midp;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.Persistable;

public class Log {
	//el detalle de la factura es un vector de vector, el segundo vector es el que tiene los objectos facturadetalle
	//asi cuando traigo un encabezado el index del detalle es el mismo
	private static PersistentObject _storeLog;
	private static VectorLogAgpsTracking vLog;
	private static final long IDStoreLog = 0xf423456L;

	static {
		_storeLog = PersistentStore.getPersistentObject(IDStoreLog);
		if(_storeLog.getContents()==null){
			vLog= new VectorLogAgpsTracking();
			_storeLog.setContents(vLog);
		}
		vLog=(VectorLogAgpsTracking)_storeLog.getContents();
	}

	public synchronized static String getLog(int index){
		return (String)vLog.elementAt(index);
	}
	
	public synchronized static java.util.Vector getLogs(){
		return vLog;
	}

	public synchronized static void addLog(String log) 
	{
		if (vLog.size()>100){
			removerTodo();
		}
		vLog.addElement(log);
		commit();
	}

	private static void commit()
	{
		_storeLog.setContents(vLog);
		_storeLog.commit();
	}    

	public synchronized static void removerLog(int index)
	{
		vLog.removeElementAt(index);
		commit();
	}

	public synchronized static void removerTodo()
	{
		try{
			if (vLog.size()>0){
				vLog.removeAllElements();
				commit();
			}
		}catch(Exception e){
			//main.Factura.log("log.remTo="+e.getMessage());
		}
	}
	
	private static class VectorLogAgpsTracking extends java.util.Vector  implements Persistable {
		
	}
}
