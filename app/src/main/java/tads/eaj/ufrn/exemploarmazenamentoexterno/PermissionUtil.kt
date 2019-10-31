package tads.eaj.ufrn.exemplopermissoes

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtil {
    companion object {
        fun validate(activity:Activity,requestCode:Int, vararg permissions:String):Boolean{

            val permissionsToRequest = ArrayList<String>()

            permissions.forEach {
                //verifica se tem a permissão
                if(ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED){
                    // verifica se alguma vez o usuário já negou permissão e precisamos dar outra explicação
                    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, it)){
                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Toast.makeText(activity, "Por favor, omi", Toast.LENGTH_SHORT).show()
                        permissionsToRequest.add(it)
                    }else{
                        // No explanation needed, we can request the permission.
                        permissionsToRequest.add(it);
                    }
                }
            }

            if (permissionsToRequest.isEmpty()) {
                // Tudo ok, retorna true
                return true
            }

            // Lista de permissões para solicitar acesso.
            val neededPermissions = permissionsToRequest.toTypedArray()

            // Solicitar permissões
            ActivityCompat.requestPermissions(activity, neededPermissions, requestCode)

            return false
        }

        fun alertAndFinish(activity:Activity, title:Int) {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(title)
                .setMessage("Para utilizar este aplicativo, você precisa aceitar as permissões.")
            // Add the buttons
            builder.setNegativeButton("Fechar"){ dialogInterface, i -> activity.finish() }
            builder.setPositiveButton("Permitir"){ dialog, id ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }
            val dialog = builder.create()
            dialog.show()
        }

    }
}