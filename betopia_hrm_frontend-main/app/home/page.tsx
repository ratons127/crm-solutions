import Navbar from '@/components/home/Navbar';
import ProductivitySection from '@/components/home/ProductivitySection';
import TrustedSection from '@/components/home/TrustedSection';
import WhyChooseSection from '@/components/home/WhyChooseSection';
import Image from 'next/image';

const HomePage = () => {
  return (
    <div>
      {/* Navbar */}
      <Navbar />
      {/* Hero Image Full Viewport */}
      <Image
        src="/images/hr-hero.png"
        alt="Hero section"
        width={5404}
        height={3892}
      />
      <TrustedSection />
      <div className="bg-purple-50">
        <div className="container mx-auto">
          <Image
            src="/images/section2.png"
            width={4479}
            height={3385}
            alt="section"
          />
        </div>
      </div>
      <WhyChooseSection />
      <ProductivitySection />
    </div>
  );
};

export default HomePage;
