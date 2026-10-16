#!/usr/bin/env bash

set -euo pipefail

APP_ID="ninjabrain-bot"
APP_NAME="Ninjabrain Bot"
REPO="Ninjabrain1/Ninjabrain-Bot"
MIN_JAVA=17

DATA_HOME="${XDG_DATA_HOME:-$HOME/.local/share}"
OPT_DIR="$HOME/.local/opt/$APP_ID"
APPS_DIR="$DATA_HOME/applications"
DESKTOP_FILE="$APPS_DIR/$APP_ID.desktop"
JAR_FILE="$OPT_DIR/$APP_ID.jar"

refresh_caches() {
    update-desktop-database "$APPS_DIR" 2>/dev/null || true
}

uninstall() {
    echo "Uninstalling $APP_NAME"
    rm -rf "$OPT_DIR"
    rm -f "$DESKTOP_FILE"
    refresh_caches
    echo "Done."
    exit 0
}

if [ "${1:-}" = "--uninstall" ]; then
    uninstall
fi

echo "Installing $APP_NAME"
echo "Checking Dependencies"

java_major() {
    local ver major
    ver=$(java -version 2>&1 | awk -F'"' '/version/ {print $2; exit}')
    major=${ver%%.*}
    if [ "$major" = "1" ]; then
        major=$(echo "$ver" | cut -d. -f2)
    fi
    echo "$major"
}

have_java() {
    command -v java >/dev/null 2>&1 && [ "$(java_major)" -ge "$MIN_JAVA" ] 2>/dev/null
}

install_java() {
    local sudo=""
    [ "$(id -u)" -ne 0 ] && sudo="sudo"

    if command -v apt-get >/dev/null 2>&1; then
        $sudo apt-get update
        $sudo apt-get install -y "openjdk-${MIN_JAVA}-jre"
    elif command -v dnf >/dev/null 2>&1; then
        $sudo dnf install -y "java-${MIN_JAVA}-openjdk"
    elif command -v pacman >/dev/null 2>&1; then
        $sudo pacman -S --needed --noconfirm jre-openjdk
    elif command -v zypper >/dev/null 2>&1; then
        $sudo zypper install -y "java-${MIN_JAVA}-openjdk"
    elif command -v apk >/dev/null 2>&1; then
        $sudo apk add "openjdk${MIN_JAVA}-jre"
    else
        echo "Couldn't detect a supported package manager." >&2
        echo "Please install Java ${MIN_JAVA}+ manually and re-run this script." >&2
        exit 1
    fi
}

if have_java; then
    echo "Found Java $(java_major)."
else
    if command -v java >/dev/null 2>&1; then
        echo "Java $(java_major) found, but version ${MIN_JAVA}+ is required."
    else
        echo "Java is not installed."
    fi

    if [ -r /dev/tty ]; then
        read -r -p "Install Java ${MIN_JAVA} now using your package manager? [y/N] " ans < /dev/tty
    else
        ans="n"
    fi

    case "$ans" in
        [yY]*) install_java ;;
        *) echo "Java ${MIN_JAVA}+ is required. Aborting."; exit 1 ;;
    esac

    have_java || { echo "Java install didn't produce a usable ${MIN_JAVA}+ runtime." >&2; exit 1; }
fi

for cmd in curl jq; do
    if command -v "$cmd" >/dev/null 2>&1; then
        echo "Found $cmd."
    else
        echo "$cmd is required but not installed." >&2
        exit 1
    fi
done

echo "Fetching latest release..."

release_json=$(curl -fsSL "https://api.github.com/repos/$REPO/releases/latest") \
    || { echo "Failed to reach the GitHub API." >&2; exit 1; }

version=$(jq -r '.tag_name // empty' <<<"$release_json")
published=$(jq -r '.published_at // empty' <<<"$release_json")
download_url=$(jq -r '[.assets[]? | select(.name | endswith(".jar"))][0].browser_download_url // empty' <<<"$release_json")

if [ -z "$version" ] || [ -z "$download_url" ]; then
    echo "Couldn't find a version or .jar asset in the latest release." >&2
    exit 1
fi

fmt_date=$(date -d "$published" "+%B %d, %Y" 2>/dev/null || echo "$published")
echo "Found latest version ($version) published on $fmt_date"

tmp_jar=$(mktemp)
trap 'rm -f "$tmp_jar"' EXIT

echo "Downloading $download_url"
curl -fL --progress-bar -o "$tmp_jar" "$download_url" \
    || { echo "Download failed." >&2; exit 1; }

mkdir -p "$OPT_DIR" "$APPS_DIR"
install -m 644 "$tmp_jar" "$JAR_FILE"
echo "$version" > "$OPT_DIR/VERSION"
echo "Installed jar to $JAR_FILE"

cat > "$DESKTOP_FILE" <<EOF
[Desktop Entry]
Type=Application
Name=$APP_NAME
Comment=Minecraft speedrunning stronghold calculator
Exec=java -jar "$JAR_FILE"
Path=$OPT_DIR
Terminal=false
Categories=Utility;Game;
EOF
chmod 644 "$DESKTOP_FILE"
echo "Created desktop entry at $DESKTOP_FILE"

refresh_caches

echo
echo "$APP_NAME $version installed. Launch it from your application menu."
echo "To uninstall run this installer with the --uninstall flag."