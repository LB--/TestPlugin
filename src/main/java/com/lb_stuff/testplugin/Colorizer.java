package com.lb_stuff.testplugin;

import org.bukkit.Bukkit;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Colorizer extends PrintStream
{
	private PrintStream old = null;
	private final ColoringOutputStream baos;
	public Colorizer()
	{
		this(null);
	}
	private Colorizer(ColoringOutputStream temp)
	{
		super(temp = new ColoringOutputStream());
		baos = temp;
		baos.setInst(this);
	}

	public void infect()
	{
		if(old == null)
		{
			old = System.out;
			System.setOut(this);
		}
	}
	public void cure()
	{
		if(old != null)
		{
			System.setOut(old);
			old = null;
		}
	}

	private void color()
	{
		cure();
		String str = baos.toString();
		/*System.out.println("{");
		for(char c : str.toCharArray())
		{
			System.out.println("  "+(int)c);
		}
		System.out.println("}");*/
		int ni = str.lastIndexOf("\r\n");
		if(ni == -1)
		{
			ni = str.lastIndexOf('\n');
		}
		if(ni != -1)
		{
			final String sub = str.substring(ni);
			if(sub.matches("\\p{Cntrl}"))
			{
				str = str.substring(0, ni) + str.substring(ni+1);
			}
			if(sub.matches("\\s*"))
			{
				str = str.substring(0, ni);
			}
		}
		if(!str.matches("\\n*"))
		{
			Bukkit.getServer().getConsoleSender().sendMessage(str);
		}
		baos.reset();
		infect();
	}

	private static class ColoringOutputStream extends ByteArrayOutputStream
	{
		private Colorizer inst = null;
		private void setInst(Colorizer c)
		{
			inst = c;
		}

		@Override
		public void flush()
		{
			try
			{
				super.flush();
			}
			catch(IOException e)
			{
				throw new RuntimeException(e);
			}
			inst.color();
		}
		@Override
		public void write(byte[] b) throws IOException
		{
			super.write(b);
			flush();
		}
		@Override
		public void write(byte[] b, int off, int len)
		{
			super.write(b, off, len);
			flush();
		}
		@Override
		public void write(int b)
		{
			super.write(b);
			flush();
		}
	}
}
