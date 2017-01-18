/*
   Copyright 2017 Thomas G. P. Nappo

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package org.jire.regedit

import com.sun.deploy.association.utility.WinRegistryWrapper.*

/**
 * A handle to a top-level key in the Windows Registry.
 *
 * @param flag The internal native access flag of the key.
 */
enum class HKEY(val flag: Int) {
	
	/**
	 * Abbreviated _HKCR_, _HKEY_CLASSES_ROOT_ contains information about registered applications,
	 * such as file associations and [OLE](https://en.wikipedia.org/wiki/Object_Linking_and_Embedding) Object Class IDs,
	 * tying them to the applications used to handle these items.
	 *
	 * On Windows 2000 and above, _HKCR_ is a compilation of user-based
	 * _HKCU\Software\Classes_ and machine-based _HKLM\Software\Classes_.
	 * If a given value exists in both of the subkeys above, the one in
	 * _HKCU\Software\Classes takes precedence_.
	 *
	 * The design allows for either machine- or user-specific registration
	 * of [COM](https://en.wikipedia.org/wiki/Component_Object_Model) objects.
	 */
	CLASSES_ROOT(HKEY_CLASSES_ROOT),
	
	/**
	 * Abbreviated _HKCU_, _HKEY_CURRENT_USER_ stores settings that are specific to the currently logged-in user.
	 * The _HKEY_CURRENT_USER_ key is a link to the subkey of _HKEY_USERS_ that corresponds to the user;
	 * the same information is accessible in both locations.
	 *
	 * The specific subkey referenced is "(HKU)\(SID)\..." where (SID) corresponds to the
	 * [Windows SID](https://en.wikipedia.org/wiki/Security_Identifier); if the "(HKCU)" key has the following suffix
	 * "(HKCU)\Software\Classes\..." then it corresponds to "(HKU)\(SID)_CLASSES\..." i.e. the suffix has the string
	 * "_CLASSES" is appended to the (SID).
	 *
	 * On Windows NT systems, each user's settings are stored in their own files called _NTUSER.DAT_ and _USRCLASS.DAT_
	 * inside their own _Documents and Settings_ subfolder (or their own _Users_ sub folder in Windows Vista and above).
	 * Settings in this hive follow users with a [roaming profile](https://en.wikipedia.org/wiki/Roaming_user_profile)
	 * from machine to machine.
	 */
	CURRENT_USER(HKEY_CURRENT_USER),
	
	/**
	 * Abbreviated _HKLM_, _HKEY_LOCAL_MACHINE_ stores settings that are specific to the local computer.
	 */
	LOCAL_MACHINE(HKEY_LOCAL_MACHINE),
	
	/**
	 * Abbreviated _HKU_, _HKEY_USERS_ contains subkeys corresponding to
	 * the _HKEY_CURRENT_USER_ keys for each user profile actively loaded on the machine,
	 * though user hives are usually only loaded for currently logged-in users.
	 */
	USERS(HKEY_USERS),
	
	/**
	 * Abbreviated _HKCC_, _HKEY_CURRENT_CONFIG_ contains information gathered at runtime;
	 * information stored in this key is not permanently stored on disk, but rather regenerated at boot time.
	 *
	 * It is a handle to the key "HKEY_LOCAL_MACHINE\System\CurrentControlSet\Hardware Profiles\Current",
	 * which is initially empty but populated at boot time by loading one of the other subkeys stored in
	 * "HKEY_LOCAL_MACHINE\System\CurrentControlSet\Hardware Profiles".
	 */
	CURRENT_CONFIG(HKEY_CURRENT_CONFIG);
	
	/**
	 * Reads a value as a string from the registry.
	 *
	 * @param key The subkey to read.
	 * @param value The value to read.
	 *
	 * @throws IllegalStateException if the value does not exist.
	 * You can use [valueExists] to check if it does before calling this function.
	 */
	operator fun get(key: String, value: String): String = WinRegQueryValueEx(flag, key, value)
	
	/**
	 * Sets a registry value to the specified content.
	 *
	 * @param key The subkey to set.
	 * @param value The value of the subkey to set.
	 * @param content The content to place within the specified value.
	 */
	operator fun set(key: String, value: String, content: String)
			= WinRegSetValueEx(flag, key, value, content) == ERROR_SUCCESS
	
	/**
	 * Returns all of the subkeys from the specified key.
	 *
	 * @param key The subkey to use.
	 */
	@JvmOverloads
	fun keys(key: String, max: Int = Int.MAX_VALUE): Array<out String>
			= WinRegGetSubKeys(flag, key, max)
	
	/**
	 * Returns all of the values from the specified key.
	 *
	 * @param key The subkey to use.
	 */
	@JvmOverloads
	fun values(key: String, max: Int = Int.MAX_VALUE): Array<out String>
			= WinRegGetValues(flag, key, max)
	
	/**
	 * Deletes a key from the registry.
	 *
	 * @param key The subkey to delete.
	 *
	 * @return Whether or not the deletion was successful.
	 */
	fun deleteKey(key: String) = WinRegDeleteKey(flag, key) == ERROR_SUCCESS
	
	/**
	 * Deletes a key from the registry.
	 *
	 * @param key The subkey to use.
	 * @param value The value to delete from the specified key.
	 *
	 * @return Whether or not the deletion was successful.
	 */
	fun deleteValue(key: String, value: String) = WinRegDeleteValue(flag, key, value) == ERROR_SUCCESS
	
	/**
	 * Checks if a key exists.
	 *
	 * @param key The subkey to check.
	 */
	fun keyExists(key: String) = WinRegSubKeyExist(flag, key)
	
	/**
	 * Checks if a value exists.
	 *
	 * @param key The subkey to use.
	 * @param value The value to check from the specified key.
	 */
	fun valueExists(key: String, value: String) = WinRegValueExist(flag, key, value)
	
	/**
	 * Creates a registry entry of the specified key.
	 *
	 * @param key The subkey to create.
	 */
	fun createKey(key: String) = WinRegCreateNoReflectionKey(flag, key) == ERROR_SUCCESS
	
}