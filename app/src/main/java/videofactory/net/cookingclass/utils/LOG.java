package videofactory.net.cookingclass.utils;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Log를 출력 처리하는 클래스이다.<br>
 * <br>
 * C에서 사용하는 printf와 비슷한 형식으로 사용할 수 있으나 제한적인 기능을 지원되며<br>
 * 지원되는 포맷 스트링은 다음과 같다.<br>
 * - "%s" : 문자열 출력<br>
 * - "%c" : 문자 1개 출력<br>
 * - "%d" : +- 부호 있는 정수 출력<br>
 * - "%f" : 실수 출력<br>
 * - "%x" : 16진수 소문자로 출력<br>
 * - "%b" : 바이트 출력<br>
 * - "%l" : 롱값 출력<br>
 * <br>
 * 로그 출력 룰은 다음과 같으며 해당 라이브러리 사용 시 반드시 이 룰을 지켜야 한다.<br>
 * - 메소드 호출 : 호출한 메소드 이름을 ">> [METHOD_NAME]()"으로 반드시 출력한다.<br>
 * - 매개/로컬/멤버 변수 출력 : "++ [VALUE_NAME] : [VALUE]"로 반드시 출력한다.<br>
 * - 에러에 대한 리턴 출력 : "-- [RETURN_REASON]"로 반드시 출력한다.<br>
 * <br>
 *  
 * <pre>
 * <code>
 * public void connectionDidFail(int type, Object result ) {
 *    LOG.info(">> connectionDidFail()");
 *    LOG.info("++ type = [%d]", type);
 *
 *    switch (type) {
 *    case NetworkRequest.CHECK_GET_EVENT_API_V4:
 *        LOG.info("-- NetworkRequest.CHECK_GET_EVENT_API_V4");
 *
 *        createNetworkConnectionFailedPopup();
 *        break;
 *    }
 *}
 * </code>
 * </pre>
 * 
 * <br>
 * 멀티 스레드 환경에서는 디버거를 통한 디버깅보다는 로그 출력을 통한 디버깅이<br>
 * 훨씬 효과적이다. 대신 빈번히 호출되는 로직은 로그 출력을 최대한 피하고, <br>
 * 중요한 로직에 효과적으로 로그를 넣는 습관을 평소에 기르도록 하자.<br>
 * 
 * @author wildsis@sbs.co.kr
 */

public class LOG {
    private static final int MAX_FILENAME_LENGTH   = 20;
    private static final int MAX_THREADNAME_LENGTH = 8;
    
	/**
	 * 태그 이름을 저장하고 있는 문자열
	 */
	private static String m_strTag = "SBSCH";

	/**
	 * 로그 레벨
	 */
	public final static int LOG_LEVEL_DEBUG = 0;
	public final static int LOG_LEVEL_ERROR = 3;
	public final static int LOG_LEVEL_FAILURE = 6;
	public final static int LOG_LEVEL_INFO = 1;
	public final static int LOG_LEVEL_TIME = 4;
	public final static int LOG_LEVEL_VERBOSE = 5;
	public final static int LOG_LEVEL_WARNING = 2;

	/**
	 * Release 모드 여부
	 */
	private static boolean m_isReleaseMode = true;

	/**
	 * Thread 이름 출력 여부
	 */
	private static boolean m_isShowThreadName = true;

	/**
	 * Time 로그 출력 여부
	 */
	private static boolean m_isShowTimeLog = false;

	/**
	 * 로그 파일 관련 오브젝트
	 */
	private static File m_objFile = null;
	private static FileOutputStream m_objFileOutStream = null;
	
	/**
	 * Synchronize 관련 오브젝트
	 */
	private static Object m_objLogLock = new Object();


    /**
     * 반복 로그를 출력한다.
     * 
     * @param args      포맷 스트링
     */
    static public void verbose(Object... args) {
        if (m_isReleaseMode) {
            return;
        }

        synchronized (m_objLogLock) {
            print(LOG_LEVEL_VERBOSE, args);
        }
    }
	
	/**
	 * 디버깅 로그를 출력한다.
	 * 
	 * @param args     포맷 스트링
	 */
	static public void debug(Object... args) {
		if (m_isReleaseMode) {
			return;
		}

		synchronized (m_objLogLock) {
			print(LOG_LEVEL_DEBUG, args);
		}
	}

    /**
     * 정보 로그를 출력한다.
     * 
     * @param args     포맷 스트링
     */
    static public void info(Object... args) {
        if (m_isReleaseMode) {
            return;
        }

        synchronized (m_objLogLock) {
            print(LOG_LEVEL_INFO, args);
        }
    }
	
	/**
	 * 에러 로그를 출력한다.
	 * 
	 * @param args     포맷 스트링
	 */
	static public void error(Object... args) {
		if (m_isReleaseMode) {
			return;
		}

		synchronized (m_objLogLock) {
			print(LOG_LEVEL_ERROR, args);
		}
	}

    /**
     * 타임 로그를 출력한다.
     * 
     * @param args      포맷 스트링
     */
    static public void time(Object... args) {
        if (m_isShowTimeLog == false) {
            return;
        }

        synchronized (m_objLogLock) {
            print(LOG_LEVEL_TIME, args);
        }
    }

    /**
     * 경고 로그를 출력한다.
     * 
     * @param args      포맷 스트링
     */
    static public void warning(Object... args) {
        if (m_isReleaseMode) {
            return;
        }

        synchronized (m_objLogLock) {
            print(LOG_LEVEL_WARNING, args);
        }
    }	

    /**
     * 현재 콜 스택을 덤프한다.
     */
    static public void dump() {
        if (m_isReleaseMode) {
            return;
        }

        synchronized (m_objLogLock) {
            Exception e = new Exception();
            printException(LOG_LEVEL_DEBUG, e);
        }
    }

    /**
     * Exception 로그를 출력한다.
     * 
     * @param e        Exception 오브젝트
     */
    static public void error(Exception e) {
        if (m_isReleaseMode) {
            return;
        }

        synchronized (m_objLogLock) {
            printException(LOG_LEVEL_ERROR, e);
        }
    }
    
    /**
     * Bundle에 저장된 키와 데이터를 출력한다.
     * 
     * @param name      출력할 번들 이름
     * @param bundle    번들 오브텍트
     */
    static public void bundle(String name, Bundle bundle) {
        if (m_isReleaseMode || bundle == null) {
            return;
        }
        LOG.info("++ %s", name);
        
        Set<String> keys = bundle.keySet();
        String key;
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
            key = iterator.next();
            LOG.info("    %s : %s", key, bundle.get(key));
        }
    }
    
	/**
	 * 출력 로그에 태그 이름을 설정한다.
	 * 
	 * @param strTag   로그 태그
	 */
	static public void setLogTag(String strTag) {
		m_strTag = strTag;
	}

	/**
	 * 릴리즈 모드를 설정한다. 릴리즈 모드가 true이더라도 error로그는 출력된다.
	 */
	static public void setReleaseMode(boolean isRelease) {
		m_isReleaseMode = isRelease;
	}

	/**
	 * 현재 릴리즈 모드를 반환한다.
	 * 
	 * @return 현재 릴리즈 모드
	 */
	static public boolean isReleaseMode() {
		return m_isReleaseMode;
	}

	/**
	 * 태그에 쓰레드 이름을 포함할지 설정한다. 쓰레드 이름이 포함될 경우 [tag].[thread_name] 형식으로 출력된다.
	 * 
	 */
	static public void showThreadName(boolean isShow) {
		m_isShowThreadName = isShow;
	}

    /**
     * 타임 로그 출력 여부를 설정한다.
     */

	static public void showTimeLog(boolean isShow) {
		m_isShowTimeLog = isShow;
	}

    /**
     * 파일 로그 출력을 시작한다.
     * 
     * 출력된 파일 로그는 SD카드(/sdcard/[SERVICENAME]/[SERVICENAME]_[DATE].log)에 저장되며
     * AndroidManifest에 "android.permission.WRITE_EXTERNAL_STORAGE"를 추가해야 한다.
     */
	static public void startFileLog(String strServiceName) {
		if (m_isReleaseMode || m_objFile != null) {
			return;
		}

		synchronized (m_objLogLock) {
			String strMountState = Environment.getExternalStorageState();
			if (strMountState.equals(Environment.MEDIA_MOUNTED) == false) {
				return;
			}

			Calendar objCal = Calendar.getInstance();
			String strDir = Environment.getExternalStorageDirectory() + "/"
					+ strServiceName;
			File objDir = new File(strDir);
			if (!objDir.isDirectory()) {
				if (!objDir.mkdir()) {
					return;
				}
			}

			String strFilePath = strDir + "/" + strServiceName + "_"
					+ String.format("%02d", objCal.get(Calendar.MONTH) + 1)
					+ String.format("%02d", objCal.get(Calendar.HOUR_OF_DAY))
					+ String.format("%02d", objCal.get(Calendar.MINUTE))
					+ String.format("%02d", objCal.get(Calendar.SECOND))
					+ ".log";
			m_objFile = new File(strFilePath);
			boolean bExist = m_objFile.exists();

			// remove
			if (bExist) {
				if (m_objFile.delete()) {
					bExist = false;
				}
			}

			// create
			if (!bExist) {
				try {
					m_objFile.createNewFile();
				} catch (IOException e) {
					close();
					return;
				}
			}

			// log
			Date objToday = new Date();
			SimpleDateFormat objDate = new SimpleDateFormat("yyyy.MM.dd");
			SimpleDateFormat objTime = new SimpleDateFormat("hh:mm:ss");
			String strDate = "=============================================================================="
					+ "\n"
					+ "Start File Logger"
					+ "\n"
					+ "Sevice Name : "
					+ strServiceName
					+ "\n"
					+ "File Path   : "
					+ strFilePath
					+ "\n"
					+ "Time        : "
					+ objDate.format(objToday)
					+ " "
					+ objTime.format(objToday)
					+ "\n"
					+ "=============================================================================="
					+ "\n";

			// write
			try {
				if (m_objFile.canWrite() == false) {
					return;
				}

				m_objFileOutStream = new FileOutputStream(m_objFile);
				write(strDate);
			} catch (IOException e) {
				close();
			}
		}
	}
	
    /**
     * 파일 로그를 출력한다
     * 
     */
    static public void endFileLog() {
        if (m_isReleaseMode == true) {
            return;
        }

        if (m_objFile == null || m_objFileOutStream == null) {
            close();
            return;
        }

        synchronized (m_objLogLock) {
            Date objToday = new Date();
            SimpleDateFormat objDate = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat objTime = new SimpleDateFormat("hh:mm:ss");
            String strDate = "=============================================================================="
                    + "\n"
                    + "Finish File Logger"
                    + "\n"
                    + "Time        : "
                    + objDate.format(objToday)
                    + " "
                    + objTime.format(objToday)
                    + "\n"
                    + "=============================================================================="
                    + "\n";
            write(strDate);
            close();
        }
    }
	
    // static method
	private static void print(int nLevel, Object[] args) {
		/*
		 * if(!FEATURES.SUPPORT_DEBUG) return;
		 */

		Thread objThread = Thread.currentThread();

		// get file name and line number
		String strFileName = objThread.getStackTrace()[4].getFileName();
		int nLineNumber = objThread.getStackTrace()[4].getLineNumber();

		// limit filename length
		if (strFileName.length() > MAX_FILENAME_LENGTH) {
			strFileName = strFileName.substring(0, MAX_FILENAME_LENGTH);
		}

		// format
		String strFormat = "" + args[0];
		strFormat = strFormat.replaceAll("%d", "%s");
		strFormat = strFormat.replaceAll("%f", "%s");
		strFormat = strFormat.replaceAll("%c", "%s");
		strFormat = strFormat.replaceAll("%b", "%s");
		strFormat = strFormat.replaceAll("%x", "%s");
		strFormat = strFormat.replaceAll("%l", "%s");

		// argument
		String strArgument = "";
		switch (args.length - 1) {
		case 0:
			strArgument = strFormat;
			break;
		case 1:
			strArgument = String.format(strFormat, "" + args[1]);
			break;
		case 2:
			strArgument = String.format(strFormat, "" + args[1], "" + args[2]);
			break;
		case 3:
			strArgument = String.format(strFormat, "" + args[1], "" + args[2],
					"" + args[3]);
			break;
		case 4:
			strArgument = String.format(strFormat, "" + args[1], "" + args[2],
					"" + args[3], "" + args[4]);
			break;
		case 5:
			strArgument = String.format(strFormat, "" + args[1], "" + args[2],
					"" + args[3], "" + args[4], "" + args[5]);
			break;
		case 6:
			strArgument = String.format(strFormat, "" + args[1], "" + args[2],
					"" + args[3], "" + args[4], "" + args[5], "" + args[6]);
			break;
		case 7:
			strArgument = String.format(strFormat, "" + args[1], "" + args[2],
					"" + args[3], "" + args[4], "" + args[5], "" + args[6], ""
							+ args[7]);
			break;
		case 8:
			strArgument = String.format(strFormat, "" + args[1], "" + args[2],
					"" + args[3], "" + args[4], "" + args[5], "" + args[6], ""
							+ args[7], "" + args[8]);
			break;
		case 9:
			strArgument = String.format(strFormat, "" + args[1], "" + args[2],
					"" + args[3], "" + args[4], "" + args[5], "" + args[6], ""
							+ args[7], "" + args[8], "" + args[9]);
			break;
		case 10:
			strArgument = String.format(strFormat, "" + args[1], "" + args[2],
					"" + args[3], "" + args[4], "" + args[5], "" + args[6], ""
							+ args[7], "" + args[8], "" + args[9], ""
							+ args[10]);
			break;
		default:
			;
		}

		// log
		String strLog = "";
		if (m_isShowThreadName) {
		    String strThreadName = objThread.getName();
	        if (strThreadName.length() > MAX_THREADNAME_LENGTH) {
	            strThreadName = strThreadName.substring(0, MAX_THREADNAME_LENGTH);
	        }
			strLog = String.format("[%-8s][%-20s:%5d] %s\n", strThreadName,
					strFileName, nLineNumber, strArgument);
		} else {
			strLog = String.format("[%-20s:%5d] %s\n", strFileName,
					nLineNumber, strArgument);
		}

		// Level
		switch (nLevel) {
		case LOG_LEVEL_ERROR:
		case LOG_LEVEL_FAILURE:
			Log.e(m_strTag, strLog);
			break;
		case LOG_LEVEL_WARNING:
			Log.w(m_strTag, strLog);
			break;
		case LOG_LEVEL_INFO:
		case LOG_LEVEL_TIME:
			Log.i(m_strTag, strLog);
			break;
		case LOG_LEVEL_VERBOSE:
			Log.v(m_strTag, strLog);
		case LOG_LEVEL_DEBUG:
		default:
			Log.d(m_strTag, strLog);
			break;
		}

		if (m_objFileOutStream != null) {
			write(strLog);
		}
	}

	static private void printException(int nLevel, Exception e) {
		if (m_isReleaseMode) {
			return;
		}

		StackTraceElement[] aElement = e.getStackTrace();

		Thread objThread = Thread.currentThread();

		// get file name and line number
		String strFileName = objThread.getStackTrace()[4].getFileName();
		int nLineNumber = objThread.getStackTrace()[4].getLineNumber();

		// limit filename length
		if (strFileName.length() > MAX_FILENAME_LENGTH) {
			strFileName = strFileName.substring(0, MAX_FILENAME_LENGTH);
		}

		int nCount = aElement.length;
		String strLog = "";
		String strThreadName = objThread.getName();
		if (strThreadName.length() > MAX_THREADNAME_LENGTH) {
		    strThreadName = strThreadName.substring(0, MAX_THREADNAME_LENGTH);
		}

		// print head line
		if (nLevel == LOG_LEVEL_ERROR) {
			strLog = String.format("[%-8s][%-20s:%5d] %s: %s", strThreadName,
					strFileName, nLineNumber, e.getClass().getName(),
					e.getMessage());
		} else {
			strLog = String.format("[%-8s][%-20s:%5d] %s", strThreadName, strFileName,
					nLineNumber, "== PRINT CALL STACK ==");
		}

		// Level
		switch (nLevel) {
		case LOG_LEVEL_ERROR:
			Log.e(m_strTag, strLog);
			break;
		default:
			Log.d(m_strTag, strLog);
			break;
		}

		if (m_objFileOutStream != null) {
			write(strLog);
		}

		// print stack trace
		for (int i = 0; i < nCount; i++) {
			if (i == 0 && nLevel != LOG_LEVEL_ERROR) {
				// do nothing
				continue;
			} else {
				strLog = String.format("%s:[%-20s:%5d]    at %s %s (%s:%d)",
						m_strTag, strFileName, nLineNumber,
						aElement[i].getClassName(),
						aElement[i].getMethodName(), aElement[i].getFileName(),
						aElement[i].getLineNumber());
			}

			// Level
			switch (nLevel) {
			case LOG_LEVEL_ERROR:
				Log.e(m_strTag, strLog);
				break;
			default:
				Log.d(m_strTag, strLog);
				break;
			}

			if (m_objFileOutStream != null) {
				write(strLog);
			}
		}
	}

	private synchronized static void write(String strLog) {
		if (m_isReleaseMode) {
			return;
		}

		if (m_objFile == null || m_objFileOutStream == null) {
			close();
			return;
		}

		try {
			if (m_objFile.canWrite() == false) {
				return;
			}

			m_objFileOutStream.write(strLog.getBytes());
		} catch (IOException e) {
			close();
		}
	}
	
    private synchronized static void close() {
        try {
            if (m_objFileOutStream != null) {
                m_objFileOutStream.close();
            }
        } catch (IOException e1) {
            ; // do nothing
        }
        m_objFileOutStream = null;
        m_objFile = null;
    }
}
