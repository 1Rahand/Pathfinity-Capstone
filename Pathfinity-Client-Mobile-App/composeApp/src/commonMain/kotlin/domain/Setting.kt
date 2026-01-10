package domain

data class Setting(
    val appearance: Appearance = Appearance.SystemDefault,
    val isFirstTime: Boolean = true,
    val lang: Lang = Lang.Krd,
    val appstoreLink: String = "https://apps.apple.com/iq/app/recheck-app/id6455685408",
    val googlePlayLink: String = "https://play.google.com/store/apps/details?id=com.enos.recheck.android",
    val desktopLink: String = "https://sheekar.app/download",
    val fibPhone: String = "7716777676",
    val whatsappPhone: String = "7716777676",
    val instagramLink: String = "https://www.instagram.com/sheekar.app/",
    val facebookLink: String = "https://www.facebook.com/profile.php?id=61569973341551",
    val onlineVersion: String = "1.0.0",
    val sheekarVersionMandatory: String = "1.0.0",
    val hasEverSyncedPrivateInfo: Boolean = false,
    val hasEverLoadInitialData: Boolean = false,
    val profileId: String = "",
){
    val whatsappLink : String get() = "https://wa.me/964$whatsappPhone"
}
