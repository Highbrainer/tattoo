
;--------------------------------
; General Attributes

!define VERSION "1.0.6"

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
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_LANGUAGE "French"

;---------------------------------
; Constants
  
  !define INSTALL_DIR "D:\apps\"
  
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
    DetailPrint "Installation des programmes dans $INSTDIR"
    SetOutPath "$INSTDIR"

    CreateDirectory "$OUTDIR"
    
    DetailPrint "Extraction de $OUTDIR\${TATTOO_ZIP}"
    ZipDLL::extractall "$OUTDIR\${TATTOO_ZIP}" "$OUTDIR" "<ALL>"
    
    DetailPrint "Extraction de $OUTDIR\${JRE18_64_ZIP}"
    ZipDLL::extractall "$OUTDIR\${JRE18_64_ZIP}" "$OUTDIR\tattoo" "<ALL>"
    
    Delete "$OUTDIR\${JRE18_64_ZIP}"    
    Delete "$OUTDIR\${TATTOO_ZIP}"

SectionEnd

Section "Raccourci" 
    DetailPrint "Création d'un raccourci dans $DESKTOP"
		CreateShortCut "$DESKTOP\Tattoo.lnk" "$INSTDIR\tattoo\tattoo.exe" "" "$INSTDIR\tattoo\tattoo.exe" 0 SW_SHOWNORMAL ALT|SHIFT|F1 "Ajout du logo MAIF sur pdf"
SectionEnd
;--------------------------------
;Installer Functions

Function .onInit

FunctionEnd