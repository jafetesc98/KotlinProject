import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    
    
    init(){
    MainViewControllerKt.doInitKoin()
    }

    @Environment(\.scenePhase) private var scenePhase

    @State var isDarkMode: Bool =
    UITraitCollection.currentTraitCollection.userInterfaceStyle == .dark
    
    var body: some Scene {
        WindowGroup {
            ContentView(isDarkMode: $isDarkMode)
            .onChange(of: scenePhase) { newScenePhase in
                if newScenePhase == .active {
                    let osTheme: UITraitCollection.TraitCollection =
                        UIScreen.mainScreen.traitCollection
                        isDarkMode = osTheme.userInterfaceStyle == .dark
                }
            }
            .prederredColorScheme(isDarkMode ? .dark : .light)
        }
    }
}

extension UIColor{
    convenience init(rgb: UInt32) {
        self.init(
            red: CGFloat((rgb & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgb & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgb & 0x0000FF) / 255.0,
            alpha: 1.0
        )
    }
}
