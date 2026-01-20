import React, { ReactNode } from 'react';
import { Button } from '@mantine/core';


type Dateish = string | Date;

export type TransferCardProps = {

  title?: string;

  tag?: string | number;


  changeText?: string;

  icon?: ReactNode;

  /** Left column */
  fromLabel?: string;          
  fromValue: string;           
  promotionDateLabel?: string; 
  promotionDate?: Dateish;     
  reasonLabel?: string;        
  reason?: string;

  /** Right column */
  toLabel?: string;            
  toValue: string;             
  effectiveDateLabel?: string; 
  effectiveDate?: Dateish;     

  /** Bottom */
  remarksLabel?: string;       
  remarks?: string;

  /** Styling hooks */
  gradientFrom?: string;       
  gradientTo?: string;
  borderColor?: string;        
  headingColor?: string;      
  className?: string;

  /** Button styling (Mantine) */
  buttonBg?: string;          
  buttonText?: string;         
  buttonProps?: React.ComponentProps<typeof Button>;
};

const formatDate = (d?: Dateish) => {
  if (!d) return null;
  if (typeof d === 'string') return d;
  try {
    return d.toLocaleDateString(undefined, { year: 'numeric', month: 'long', day: 'numeric' });
  } catch {
    return String(d);
  }
};

const TransferCard: React.FC<TransferCardProps> = ({
  title ,
  tag,
  changeText ,
  icon,

  fromLabel = 'From',
  fromValue,
  promotionDateLabel = 'Promotion Date',
  promotionDate,
  reasonLabel = 'Reason',
  reason,

  toLabel = 'To',
  toValue,
  effectiveDateLabel = 'Effective Date',
  effectiveDate,

  remarksLabel = 'Remarks',
  remarks,

  gradientFrom = '#F0FDF4',
  gradientTo = '#ECFDF5',
  borderColor = '#BEDBFF',
  headingColor = '#016630',
  className = '',

  buttonBg = '#00C950',
  buttonText = '#FFFFFF',
  buttonProps,
}) => {
  // Tailwind: use arbitrary values for gradient + border color
  const wrapperClass =
    `bg-[linear-gradient(90deg,_${gradientFrom}_0%,_${gradientTo}_100%)] ` +
    `w-full px-10 py-8 rounded-2xl border ` + // use "border" (Tailwind doesn’t have border-1)
    `border-[${borderColor}]`;

  return (
    <div className={className}>
      <div className={wrapperClass}>
        {/* Header */}
        <div className="grid grid-cols-1 sm:grid-cols-2 items-center">
          <div className="flex flex-wrap gap-3 items-center justify-start">
            <div className="text-[18px]" style={{ color: headingColor }}>
              <div className="flex items-center gap-3">
                <span className="text-[18px]">{icon}</span>
                <span className="text-[18px]">{title}</span>
                {tag != null && <span className="text-[18px]">#{tag}</span>}
              </div>
            </div>
          </div>

          <div className="flex place-content-end">
            <Button
              variant="filled"
              radius="md"
              size="sm"
              styles={{ root: { backgroundColor: buttonBg }, label: { color: buttonText } }}
              {...buttonProps}
            >
              {changeText}
            </Button>
          </div>
        </div>

        {/* Middle section */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 border-b border-[#BEDBFF] py-5">
          <div className="space-y-3">
            <div>
              <h5 className="text-sm text-gray-600">{fromLabel}:</h5>
              <h4 className="font-medium text-gray-900">{fromValue}</h4>
            </div>
            {promotionDate && (
              <div>
                <h5 className="text-sm text-gray-600">{promotionDateLabel}:</h5>
                <h4 className="font-medium text-gray-900">{formatDate(promotionDate)}</h4>
              </div>
            )}
            {reason && (
              <div>
                <h5 className="text-sm text-gray-600">{reasonLabel}:</h5>
                <h4 className="font-medium text-gray-900">{reason}</h4>
              </div>
            )}
          </div>

          <div className="space-y-3">
            <div>
              <h5 className="text-sm text-gray-600">{toLabel}:</h5>
              <h4 className="font-medium text-gray-900">{toValue}</h4>
            </div>
            {effectiveDate && (
              <div>
                <h5 className="text-sm text-gray-600">{effectiveDateLabel}:</h5>
                <h4 className="font-medium text-gray-900">{formatDate(effectiveDate)}</h4>
              </div>
            )}
          </div>
        </div>

        {/* Bottom */}
        {remarks && (
          <div className="py-3">
            <h5 className="text-sm text-gray-600">{remarksLabel}:</h5>
            <h5 className="font-medium text-gray-900">
              {remarks}
            </h5>
          </div>
        )}
      </div>
    </div>
  );
};

export default TransferCard;
