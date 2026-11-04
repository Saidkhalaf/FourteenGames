import {useColorScheme} from "@mui/material/styles";
import SettingsBrightnessIcon from '@mui/icons-material/SettingsBrightness';
import DarkModeIcon from '@mui/icons-material/DarkMode';
import LightModeIcon from '@mui/icons-material/LightMode';
import {ToggleButton, ToggleButtonGroup, Tooltip, useTheme} from "@mui/material";

export function ThemeControls(){
    const {mode, setMode} = useColorScheme();
    const theme = useTheme();

    if (!mode) return null;

    const handleChange = (
        _event: React.MouseEvent<HTMLElement>,
        newMode: "system" | "light" | "dark" | null
    ) => {
        if (newMode) setMode(newMode);
    };

    return (
        <ToggleButtonGroup
            value={mode}
            exclusive
            onChange={handleChange}
            size="small"
            sx={{color:theme.palette.primary.main}}
        >
            <Tooltip title="System">
                <ToggleButton value="system"
                              aria-label="system mode">
                    <SettingsBrightnessIcon sx={{color:theme.palette.primary.main}}/>
                </ToggleButton>
            </Tooltip>

            <Tooltip title="Light">
                <ToggleButton value="light"
                              aria-label="light mode">
                    <LightModeIcon sx={{color:theme.palette.primary.main}}/>
                </ToggleButton>
            </Tooltip>

            <Tooltip title="Dark">
                <ToggleButton value="dark"
                              aria-label="dark mode">
                    <DarkModeIcon sx={{color:theme.palette.primary.main}}/>
                </ToggleButton>
            </Tooltip>
        </ToggleButtonGroup>
    );
}
