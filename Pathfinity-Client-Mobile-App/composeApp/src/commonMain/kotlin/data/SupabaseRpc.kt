package data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put


suspend fun SupabaseClient.rpcRedeemGiftCard(giftCardCode: String): Boolean {
   val json = buildJsonObject { put("p_gift_code", giftCardCode) }
   return this.postgrest.rpc("rpc_redeem_gift_card", json).decodeAs<Boolean>()
}

suspend fun SupabaseClient.rpcGetDecryptionKey(): String {
   return this.postgrest.rpc("get_decryption_key").decodeAs<String>()
}

suspend fun SupabaseClient.rpcCanSignIn(deviceId: String, email: String): String {
   val json = buildJsonObject { put("p_device_id", deviceId); put("p_email", email) }
   return this.postgrest.rpc("can_sign_in", json).decodeAs<String>()
}

suspend fun SupabaseClient.rpcSetProfileDeviceInfo(osName: String?, deviceName: String?, deviceId: String?) {
   val json = buildJsonObject { put("p_os_name", osName); put("p_device_name", deviceName); put("p_device_id", deviceId) }
   this.postgrest.rpc("rpc_set_profile_device_info", json)
}

suspend fun SupabaseClient.rpcGenerateGiftCards(amount: Int) : List<String>{
   val json = buildJsonObject { put("p_quantity", amount) }
   return this.postgrest.rpc("generate_and_return_gift_cards", json).decodeList()
}

suspend fun SupabaseClient.rpcGenerateGiftCards(amount: Int , metadata : String) : List<String>{
   val json = buildJsonObject {
      put("p_quantity", amount)
      put("p_metadata" , metadata)
   }
   return this.postgrest.rpc("generate_and_return_gift_cards_metadata", json).decodeList()
}

suspend fun SupabaseClient.rpcDeleteAccount() {
   this.postgrest.rpc("rpc_delete_account")
}

suspend fun SupabaseClient.rpcGetTotalUsers() : Int {
   return this.postgrest.rpc("get_total_users").decodeAs<Int>()
}

suspend fun SupabaseClient.rpcGetTotalAnsweredQuestionsByAllUsers() : Int {
   return this.postgrest.rpc("get_total_answered_questions_by_all_users").decodeAs<Int>()
}