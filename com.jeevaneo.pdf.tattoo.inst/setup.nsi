
;--------------------------------
; General Attributes

!define VERSION "1.0.4"

Name "Pdf Tattoo for Maif"
OutFile "download\tattoo-inst-v${VERSION}.exe"
;SilentInstall silent
RequestExecutionLevel user

!addplugindir ".\nsis"

;--------------------------------
;Interface Settings

  !include "MUI2.nsh"
  !define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install-colorful.ico"
  !insertmacro MUI_PAGE_WELCOME
  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_LANGUAGE "French"

;---------------------------------
; Constants
  
  !define INSTALL_DIR "C:\apps\"
  
  !define JRE18_64_ZIP "jre.zip"
  !define TATTOO_ZIP "tattoo-${VERSION}.zip"

;SetFont 14


InstallDir "${INSTALL_DIR}"

;--------------------------------
;Installer Sections
Section "Principal"
 	SetOutPath $INSTDIR
    File ".\content\${TATTOO_ZIP}"
    File ".\content\${JRE18_64_ZIP}"
SectionEnd

Section "Telechargement" SecCommons
    DetailPrint "Installation des programmes"
    SetOutPath "${INSTALL_DIR}"

    CreateDirectory "$OUTDIR"
    
;    DetailPrint "Téléchargement du JDK 1.8 64bits"
;    inetc::get /caption "JDK 1.8 64bits" /popup "" "http://sources-fabfonc.maif.local/svn/socle/framework/apd/inst/trunk/maif.releng.inst/installer-content/${JDK18_64_ZIP}" "$OUTDIR\${JDK18_64_ZIP}" /end

;    Pop $0 # return value = exit code, "OK" means OK

;    MessageBox MB_OK "Téléchargement - Status: $0"
    
    DetailPrint "Extraction de $OUTDIR\${TATTOO_ZIP}"
    ZipDLL::extractall "$OUTDIR\${TATTOO_ZIP}" "$OUTDIR" "<ALL>"
    
    DetailPrint "Extraction de $OUTDIR\${JRE18_64_ZIP}"
    ZipDLL::extractall "$OUTDIR\${JRE18_64_ZIP}" "$OUTDIR\tattoo" "<ALL>"
    
    Delete "$OUTDIR\${JRE18_64_ZIP}"    
    Delete "$OUTDIR\${TATTOO_ZIP}"

SectionEnd

Section "Raccourci"
;    ReadEnvStr $USERPROFILE USERPROFILE
		CreateShortCut "$%USERPROFILE%\Desktop\Tattoo.lnk" "$INSTDIR\tattoo\tattoo.exe" "" "$INSTDIR\tattoo\tattoo.exe" 0 SW_SHOWNORMAL ALT|SHIFT|F1 "Ajout du logo MAIF sur pdf"
SectionEnd
;--------------------------------
;Installer Functions

Function .onInit

; plug-in auto-recognizes 'no parent dlg' in onInit and works accordingly
;    inetc::head /RESUME "Network error. Retry?" "http://ineum.narod.ru/spr_2003.htm" "$EXEDIR\spr3.txt"
;    Pop $4

FunctionEnd