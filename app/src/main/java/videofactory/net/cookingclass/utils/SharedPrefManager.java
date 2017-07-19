package videofactory.net.cookingclass.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager
{
    private static SharedPrefManager instance   = null;
    private static SharedPreferences preference = null;
    private static String filename   = null;

    public static SharedPrefManager getInstance(Context _context, String _filename)
    {
        if (instance == null)
        {

            instance = new SharedPrefManager(_context, _filename);

        }
        return instance;
    }

    public SharedPrefManager(Context _context, String _filename)
    {
        filename = _filename;
        if (filename == null || "".equals(filename))
            filename = "preference_data";
        preference = _context.getSharedPreferences(filename, Context.MODE_PRIVATE);
    }

    /**
     * SharedPreference�� ��簪�� �����Ѵ�.
     * 
     * @return ��������
     */
    public boolean clearSharedValue()
    {

        try
        {
            if (preference != null)
            {
                SharedPreferences.Editor editor = preference.edit();
                editor.clear();
                editor.commit();
            }
        } catch (Exception e)
        {
            return false;
        }
        return true;
    }

    public boolean getSharedValueByBoolean(String _key, boolean _default)
    {
        try
        {
            return preference.getBoolean(_key, _default);
        } catch (Exception e)
        {
            return _default;
        }
    }

    public float getSharedValueByFloat(String _key, float _default)
    {
        try
        {
            return preference.getFloat(_key, _default);
        } catch (Exception e)
        {
            return _default;
        }
    }

    public int getSharedValueByInt(String _key, int _default)
    {
        try
        {
            return preference.getInt(_key, _default);
        } catch (Exception e)
        {
            return _default;
        }
    }

    public String getSharedValueByString(String _key, String _default)
    {
        try
        {
            return preference.getString(_key, _default);
        } catch (Exception e)
        {
            return _default;
        }
    }
    public void setSharedValueByBoolean(String _key, boolean _value)
    {
        try
        {
            SharedPreferences.Editor editor = preference.edit();
            editor.putBoolean(_key, _value);
            editor.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setSharedValueByFloat(String _key, float _value)
    {
        try
        {
            SharedPreferences.Editor editor = preference.edit();
            editor.putFloat(_key, _value);
            editor.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setSharedValueByInt(String _key, int _value)
    {
        try
        {
            SharedPreferences.Editor editor = preference.edit();
            editor.putInt(_key, _value);
            editor.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setSharedValueByLong(String _key, long _value)
    {
        try
        {
            SharedPreferences.Editor editor = preference.edit();
            editor.putLong(_key, _value);
            editor.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void setSharedValueByString(String _key, String _value)
    {
        try
        {
            SharedPreferences.Editor editor = preference.edit();
            editor.putString(_key, _value);
            editor.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
