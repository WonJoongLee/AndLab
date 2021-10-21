package kr.wonjoong.broadcastreceiverpractice1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BroadCastReceiver : BroadcastReceiver() {

    val MyAction = "kr.wonjoong.broadcastreceiverpractice1.ACTION_MY_BROADCAST"

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        // 전원 연결 및 전원 해제 시 Toast 메시지를 띄운다.
        when(intent.action){
            Intent.ACTION_POWER_CONNECTED-> {
                Toast.makeText(context, "전원 연결", Toast.LENGTH_SHORT).show()
            }
            Intent.ACTION_POWER_DISCONNECTED->{
                Toast.makeText(context, "전원 연결 해제", Toast.LENGTH_SHORT).show()
            }
        }
    }
}