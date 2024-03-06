package pe.com.domain.acs.api.consumer.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que contiene metodos utilitarios
 * 
 * @author Domain Consulting
 * @version 1.0
 */
public class Util {

	/**
	 * Metodo que obtiene la extension del archivo a traves de su nombre.
	 * 
	 * @param fileName Nombre del archivo.
	 * @return Devuelve la extension del archivo.
	 */
	public static String getFileExtension(String fileName) {
		if (fileName.contains(".")) {
			String arr[] = fileName.split("\\.");
			return arr[arr.length - 1].toLowerCase();
		} else {
			return null;
		}
	}

	/**
	 * Metodo que valida si la cadena enviada es vacia o nula.
	 * 
	 * @param ptext Cadena a procesar.
	 * @return Devuelve true si la cadena enviada es nula o vacia. Devuelve false en
	 *         caso contrario.
	 */
	public static boolean isBlank(String ptext) {
		return ptext == null || ptext.trim().length() == 0;
	}

	/**
	 * Metodo que valida si el Hashmap enviado es vacio o nulo.
	 * 
	 * @param map Hashmap a procesar.
	 * @return Devuelve true si el Hashmap enviado es vacio o nulo. Devuelve false
	 *         en caso contrario.
	 */
	public static boolean isBlankMap(HashMap<String, String> map) {
		return map == null || map.size() == 0;
	}

	/**
	 * Metodo que valida si la cadena enviada contiene caracteres invalidados para
	 * el nombre del documento en Alfresco.
	 * 
	 * @param toExamine Cadena a examinar.
	 * @return Devuelve true si la cadena contiene caracteres invalidos. Devuelve
	 *         false en caso contrario.
	 */
	public static boolean containsIllegals(String toExamine) {
		boolean resp = true;
		if (!Util.isBlank(toExamine)) {
			Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\^]");
			Matcher matcher = pattern.matcher(toExamine);
			resp = matcher.find();
		}
		return resp;
	}

	/**
	 * Metodo que valida si la cadena enviada contiene caracteres invalidados para
	 * el nombre de la carpeta en Alfresco.
	 * 
	 * @param folderName Cadena a examinar.
	 * @return Devuelve true si la cadena contiene caracteres invalidos. Devuelve
	 *         false en caso contrario.
	 */
	public static boolean containsIllegalsFolderName(String folderName) {
		boolean bandera = false;
		String[] caracteres = { "*", "\"", "<", ">", "\\", "//", "?", ":", "|", "." };
		for (int i = 0; i < caracteres.length - 1; i++) {
			int pos = folderName.indexOf(caracteres[i]);
			if (pos != -1) {
				bandera = true;
			}
		}
		if (folderName.substring(folderName.length() - 1).equals(caracteres[caracteres.length - 1])) {
			bandera = true;
		}
		return bandera;
	}

	/**
	 * Metodo que valida y completa la cadena que representa una ruta en Alfresco.
	 * 
	 * @param pathFolder Cadena a examinar.
	 * @return Devuelve la cadena validada y completa.
	 */
	public static String formatPathFolder(String pathFolder) {
		if (pathFolder.substring(0, 1).equals("/")) {
			pathFolder = pathFolder.substring(1);
		}
		if (pathFolder.substring(pathFolder.length() - 1).equals("/")) {
			pathFolder = pathFolder.substring(0, pathFolder.length() - 1);
		}
		return pathFolder;
	}

}
