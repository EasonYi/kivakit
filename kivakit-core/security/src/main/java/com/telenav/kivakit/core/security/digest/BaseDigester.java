////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.security.digest;

import com.telenav.kivakit.core.security.project.lexakai.diagrams.DiagramSecurityDigest;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UmlClassDiagram(diagram = DiagramSecurityDigest.class)
public abstract class BaseDigester implements Digester
{
    private final String algorithmName;

    protected BaseDigester(final String algorithmName)
    {
        this.algorithmName = algorithmName;
    }

    @Override
    public byte[] digest(final byte[] value)
    {
        try
        {
            return MessageDigest.getInstance(algorithmName).digest(value);
        }
        catch (final NoSuchAlgorithmException e)
        {
            throw new IllegalStateException("Can't create digest", e);
        }
    }
}
