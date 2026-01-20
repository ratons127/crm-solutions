import React from 'react';
import * as AiIcons from 'react-icons/ai';
import * as BiIcons from 'react-icons/bi';
import * as BsIcons from 'react-icons/bs';
import * as CgIcons from 'react-icons/cg';
import * as CiIcons from 'react-icons/ci';
import * as FaIcons from 'react-icons/fa';
import * as Fa6Icons from 'react-icons/fa6';
import * as FiIcons from 'react-icons/fi';
import * as GrIcons from 'react-icons/gr';
import * as HiIcons from 'react-icons/hi';
import * as Hi2Icons from 'react-icons/hi2';
import * as ImIcons from 'react-icons/im';
import * as IoIcons from 'react-icons/io5';
import * as LiaIcons from 'react-icons/lia';
import * as LuIcons from 'react-icons/lu';
import * as MdIcons from 'react-icons/md';
import * as PiIcons from 'react-icons/pi';
import * as RxIcons from 'react-icons/rx';
import { RxDashboard } from 'react-icons/rx';
import * as TbIcons from 'react-icons/tb';
import * as VscIcons from 'react-icons/vsc';

/**
 * Dynamic Icon Resolver - React Icons Only
 *
 * This function takes an icon name from the API and dynamically renders it
 * without needing to manually add each icon to a map.
 *
 * Supports all react-icons libraries:
 * - Font Awesome (FA, FA6): FaUser, FaHome, FaCog, etc.
 * - VS Code Icons (VSC): VscTools, VscHome, VscGear, etc.
 * - Radix (RX): RxDashboard, RxPerson, RxGear, etc.
 * - Circum Icons (CI): CiSettings, CiLocationOn, CiCalendar, etc.
 * - Material Design (MD): MdSettings, MdHome, MdPeople, etc.
 * - Lucide (LU): LuCalendar, LuHome, LuUser, etc.
 * - Ant Design (AI): AiOutlineHome, AiOutlineUser, etc.
 * - Bootstrap Icons (BI, BS): BiHome, BsGear, etc.
 * - Feather Icons (FI): FiHome, FiUser, FiSettings, etc.
 * - Ionicons (IO): IoHome, IoSettings, IoPerson, etc.
 * - Hero Icons (HI, HI2): HiHome, HiUser, etc.
 * - Tabler Icons (TB): TbHome, TbUser, TbSettings, etc.
 *
 * @param iconName - The name of the icon (e.g., "VscTools", "FaUser", "RxDashboard")
 * @param className - Optional className for styling (default: "h-5 w-5")
 * @returns React element of the icon or a fallback icon
 */
export const DynamicIcon = ({
  iconName,
  className = 'h-5 w-5',
}: {
  iconName?: string;
  className?: string;
}) => {
  if (!iconName) {
    // Default fallback icon from react-icons
    return <RxDashboard className={className} />;
  }

  // Combine all react-icons libraries
  const iconLibraries: Record<string, any> = {
    ...FaIcons,
    ...Fa6Icons,
    ...RxIcons,
    ...CiIcons,
    ...MdIcons,
    ...LuIcons,
    ...VscIcons,
    ...AiIcons,
    ...BiIcons,
    ...BsIcons,
    ...FiIcons,
    ...IoIcons,
    ...HiIcons,
    ...Hi2Icons,
    ...TbIcons,
    ...GrIcons,
    ...PiIcons,
    ...LiaIcons,
    ...CgIcons,
    ...ImIcons,
  };

  // Try to find the icon by exact name
  const IconComponent = iconLibraries[iconName];

  if (IconComponent) {
    return <IconComponent className={className} />;
  }

  // Fallback: try to find by case-insensitive match
  const iconNameLower = iconName.toLowerCase();
  const matchedKey = Object.keys(iconLibraries).find(
    key => key.toLowerCase() === iconNameLower
  );

  if (matchedKey) {
    const MatchedIcon = iconLibraries[matchedKey];
    return <MatchedIcon className={className} />;
  }

  // If still not found, return default icon
  console.warn(`Icon "${iconName}" not found. Using default icon.`);
  return <RxDashboard className={className} />;
};

/**
 * Get icon component by name (for use in useMemo)
 */
export const getIconComponent = (iconName?: string): React.ReactNode => {
  return <DynamicIcon iconName={iconName} />;
};
